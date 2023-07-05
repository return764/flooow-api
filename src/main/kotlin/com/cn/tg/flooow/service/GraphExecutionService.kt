package com.cn.tg.flooow.service

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionVO
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.model.dag.DirectedAcyclicGraph
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.lang.reflect.Field


@Component
class GraphExecutionService(
    private val graphService: GraphService,
    private val template: SimpMessagingTemplate,
    private val taskScheduler: TaskScheduler
) {

    fun execute(graphId: String) {
        val graphData = graphService.getGraphData()
        val executionDAG = buildGraph(graphData)
        val context = TaskContext(graphService, template, executionDAG)
        taskScheduler.schedule(context)
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

class ExecutionDAG(tasks: List<Task>, edges: List<TaskEdge>) {
    // 还应该包含当前正在运行的图的id
    private val dag: DirectedAcyclicGraph<Task> = DirectedAcyclicGraph()
    // 应该维护一个队列，旧的任务执行完成后，将新的任务添加到队列中
    private val currentTasks: MutableList<Task> by lazy { dag.getFirsts().toMutableList() }

    init {
        tasks.forEach {
            this.dag.addNode(it)
        }
        edges.forEach {
            this.dag.addEdge(it.source, it.target)
        }
    }

    fun loadRunnableTasks(): List<Action> {
        return currentTasks.map { loadExecutionTask(it) }
    }

    private fun loadExecutionTask(t: Task): Action {
        val clazz = Class.forName(t.action.className)
        val task = clazz.getDeclaredConstructor().newInstance() as Action
        val optionName2Field = getDeclaredFieldWithActionMarker(task.javaClass)
        t.options.forEach {
            optionName2Field[it.label]?.trySetAccessible()
            optionName2Field[it.label]?.set(task, it.value)
        }
        return task
    }

    fun findNextTasks(): List<Task> {
        return findNextTasks(currentTasks).also {
            currentTasks.clear()
            currentTasks.addAll(it)
        }
    }

    private fun findNextTasks(tasks: List<Task>): List<Task> {
        return tasks.map {
            this.dag.next(it).orElse(listOf())
        }
            .flatten()
            .distinct()
    }

    private fun getDeclaredFieldWithActionMarker(clazz: Class<*>): Map<String, Field> {
        return clazz.declaredFields.filter {
            it.isAnnotationPresent(ActionOption::class.java)
        }.associateBy { it.getAnnotation(ActionOption::class.java).name }
    }
}

@Component
class TaskScheduler {
    // 并发和协程
    fun schedule(ctx: TaskContext) = runBlocking {
        launch {
            while (true) {
                val tasks = ctx.dag.loadRunnableTasks()
                tasks.map {
                    async { it.run(ctx) }
                }.forEach { it.await() }

                if (ctx.dag.findNextTasks().isEmpty()) {
                    return@launch
                }
            }
        }
    }
}



class TaskContext(
    val graphService: GraphService,
    val template: SimpMessagingTemplate,
    val dag: ExecutionDAG
) {

}
