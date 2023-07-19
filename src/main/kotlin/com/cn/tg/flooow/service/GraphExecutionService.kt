package com.cn.tg.flooow.service

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionVO
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.dag.DirectedAcyclicGraph
import com.cn.tg.flooow.service.handler.ActionOptionFillingHandlers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis


@Component
class GraphExecutionService(
    private val graphService: GraphService,
    private val template: MessageHandler,
    private val taskExecutor: TaskExecutor
) {

    fun execute(graphId: String) {
        val graphData = graphService.getGraphData()
        val executionDAG = buildGraph(graphData)
        val context = TaskContext(graphService, template, executionDAG)
        taskExecutor.execute(context)
    }

    private fun buildGraph(graphData: GraphDataVO): ExecutionDAG {
        val tasksMap = graphData.nodes.associate {
            it.id to Task(it, graphService.getActionOptions(it.id), graphService.getAction(it.id))
        }
        val taskEdges = graphData.edges.map {
            TaskEdge(tasksMap[it.source.cell]!!, tasksMap[it.target.cell]!!)
        }
        return ExecutionDAG(tasksMap.values.toList(), taskEdges)
    }

}

data class Task(
    val node: Node,
    val options: List<ActionOptionVO>,
    val action: ActionVO
)

data class TaskEdge(
    val source: Task,
    val target: Task
)

data class ExecutionTask(
    val task: Task,
    val action: Action
)

class ExecutionDAG(tasks: List<Task>, edges: List<TaskEdge>) {
    // 还应该包含当前正在运行的图的id
    private val dag: DirectedAcyclicGraph<Task> = DirectedAcyclicGraph()

    private val tasksCache = CopyOnWriteArrayList<ExecutionTask>()

    init {
        tasks.forEach {
            this.dag.addNode(it)
        }
        edges.forEach {
            this.dag.addEdge(it.source, it.target)
        }
    }

    fun getFirstTasks(): List<ExecutionTask> {
        return loadTasks(dag.getFirsts())
    }

    private fun loadTasks(tasks: List<Task>): List<ExecutionTask> {
        return tasks.map { buildExecutionTask(it) }
    }

    private fun buildExecutionTask(t: Task): ExecutionTask {
        val clazz = Class.forName(t.action.className)
        val task = clazz.getDeclaredConstructor().newInstance() as Action
        return ExecutionTask(t, task).also { tasksCache.add(it) }
    }

    fun findNextTasks(task: ExecutionTask): List<ExecutionTask> {
        val tasks = dag.next(task.task).orElse(listOf())

        val cached = tasksCache.filter { tasks.contains(it.task) }
        if (cached.isNotEmpty()) {
            return cached
        }

        return loadTasks(tasks)
    }

    fun findPreviousTasks(task: ExecutionTask): List<ExecutionTask> {
        val tasks = dag.previous(task.task).orElse(listOf())
        tasks.filter {t -> tasksCache.find { it.task == t } == null }.forEach { buildExecutionTask(it) }
        return tasksCache.filter { tasks.contains(it.task) }
    }

    fun findTaskByAction(action: Action): ExecutionTask? {
        return tasksCache.find { it.action == action }
    }

    fun getLastTasks(): List<ExecutionTask> {
        val tasks = dag.getLasts()
        val cache = tasksCache.filter { tasks.contains(it.task) }
        if (cache.isNotEmpty()) {
            return cache
        }
        return loadTasks(tasks)
    }
}


@Component
class TaskExecutor(private val optionHandlers: ActionOptionFillingHandlers) {

    fun execute(ctx: TaskContext) = runBlocking {
        val monitor = TaskMonitor(optionHandlers)
        val listener = monitor.startListener(ctx) {
            it.cleanUpAll()
        }
        ctx.firstsTasks().forEach {
            monitor.monitor(it, ctx)
        }
        listener.join()

    }

}

class TaskMonitor(private val optionFillingHandler: ActionOptionFillingHandlers) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    private val waitingTasks = CopyOnWriteArrayList<ExecutionTask>()
    private val completedTasks = CopyOnWriteArrayList<ExecutionTask>()
    private val jobCoroutineMap = ConcurrentHashMap<ExecutionTask, Job>()
    private val nextTaskChannel = Channel<ExecutionTask>()
    private val mutex = Mutex()

    private suspend fun notifyStart(task: ExecutionTask) {
        jobCoroutineMap[task]?.let {
            if (!it.isCompleted && !it.isActive) {
                it.start()
                it.join()
            } else {
                return
            }
        }
        notifyNext(task)
    }

    private suspend fun notifyNext(task: ExecutionTask) {
        mutex.withLock {
            completedTasks.add(task)
            nextTaskChannel.send(task)
        }
    }

    suspend fun startListener(ctx: TaskContext, post: ((monitor: TaskMonitor) -> Unit)?): Job {
        return ctx.launch {
            while (true) {
                val task = nextTaskChannel.receive()
                val nextTasks = ctx.loadNextExecutionActions(task.action).union(waitingTasks)
                if (nextTasks.isEmpty() && completedTasks.containsAll(ctx.lastsTasks())) {
                    break
                }
                nextTasks.forEach { monitor(it, ctx) }
            }
            nextTaskChannel.close()
            post?.invoke(this@TaskMonitor)
        }
    }

    fun cleanUpAll() {
        waitingTasks.clear()
        completedTasks.clear()
        jobCoroutineMap.clear()
    }

    fun monitor(task: ExecutionTask, ctx: TaskContext) {
        ctx.launch wrap@{
            mutex.withLock {
                waitingTasks.addIfAbsent(task)
                val previous = ctx.previousTasks(task)
                val previousJobs = jobCoroutineMap.filter { previous.contains(it.key) }
                if (previousJobs.size < previous.size) {
                    return@wrap
                }
                previousJobs.forEach {
                    if (!it.value.isCompleted) {
                        return@wrap
                    }
                }
            }

            val job = buildJob(ctx, task)
            jobCoroutineMap.putIfAbsent(task, job)
            waitingTasks.remove(task)
            notifyStart(task)
        }
    }

    private fun buildJob(
        ctx: TaskContext,
        task: ExecutionTask
    ): Job {
        val job = ctx.launch(start = CoroutineStart.LAZY) {
            optionFillingHandler.apply(ctx, task)

            val timer = measureTimeMillis {
                kotlin.runCatching {
                    task.action.run(ctx)
                }.onFailure {
                    logger.info("Error occur when execute task... $it")
                    cleanUpAll()
                    cancel()
                    return@launch
                }
            }
            logger.info("Task [${task.task.action.templateName}] execution successful, cost $timer millis")
        }
        return job
    }

}


class TaskContext(
    private val graphService: GraphService,
    private val template: MessageHandler,
    private val dag: ExecutionDAG,
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    private val taskRuntimeResultStore = ConcurrentHashMap<ExecutionTask, String>()

    fun getService(): GraphService {
        return this.graphService
    }

    fun getMessagingHandler(): MessageHandler {
        return this.template
    }

    fun firstsTasks(): List<ExecutionTask> {
        return dag.getFirstTasks()
    }

    fun lastsTasks(): List<ExecutionTask> {
        return dag.getLastTasks()
    }

    fun previousTasks(task: ExecutionTask): List<ExecutionTask> {
        return dag.findPreviousTasks(task)
    }

    fun currentTask(action: Action): ExecutionTask {
        return dag.findTaskByAction(action)!!
    }

    fun loadNextExecutionActions(action: Action): List<ExecutionTask> {
        return dag.findNextTasks(currentTask(action))
    }

    fun receiveValue(action: Action): Map<ExecutionTask, String> {
        return previousTasks(currentTask(action)).associateWith { taskRuntimeResultStore[it].orEmpty() }
    }

    fun returnValue(action: Action, value: String) {
        taskRuntimeResultStore[currentTask(action)] = value
    }
}
