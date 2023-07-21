package com.cn.tg.flooow.service

import com.cn.tg.flooow.enums.ActionStatus
import com.cn.tg.flooow.enums.ReturnType
import com.cn.tg.flooow.exceptions.TaskException
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageHandler(
    private val template: SimpMessagingTemplate
) {

    companion object {
        const val RETURN_TYPE = "return-type"
    }

    fun sendWithHeader(destination: String, payload: Any?, header: Map<String, Any>) {
        template.convertAndSend(destination, payload ?: "", header)
    }

    fun builder(): MessageBuilder {
        return MessageBuilder(this)
    }

    fun create(ctx:TaskContext, task: ExecutionTask): TaskMessageHandler {
        return TaskMessageHandler(this, ctx, task)
    }


    class MessageBuilder(
        private val handler: MessageHandler,
    ) {
        private lateinit var destination: String
        private val header: MutableMap<String, Any> = mutableMapOf()
        private var payload: Any? = null

        fun header(key: String, value: Any): MessageBuilder {
            this.header[key] = value
            return this
        }

        fun payload(value: Any?): MessageBuilder {
            this.payload = value
            return this
        }

        fun destination(value: String): MessageBuilder {
            this.destination = value
            return this
        }

        fun returnType(type: ReturnType): MessageBuilder {
            return header(RETURN_TYPE, type)
        }

        fun send() {
            handler.sendWithHeader(destination, payload, header)
        }
    }
}

class TaskMessageHandler(
    private val messageHandler: MessageHandler,
    private val ctx: TaskContext,
    private val executionTask: ExecutionTask) {

    private fun MessageHandler.MessageBuilder.destination(ctx: TaskContext): MessageHandler.MessageBuilder {
        return this.destination(TaskContext.GRAPH_RUNTIME_PATH + ctx.graphId)
    }

    private fun MessageHandler.MessageBuilder.status(status: ActionStatus): MessageHandler.MessageBuilder {
        return this.header("status", status)
    }

    private fun MessageHandler.MessageBuilder.nodeId(executionTask: ExecutionTask): MessageHandler.MessageBuilder {
        return this.header("node-id", executionTask.task.node.id)
    }

    fun sendOnReady() {
        messageHandler.builder()
            .destination(ctx)
            .status(ActionStatus.ON_READY)
            .nodeId(executionTask)
            .send()
    }

    fun sendRunning() {
        messageHandler.builder()
            .destination(ctx)
            .status(ActionStatus.RUNNING)
            .nodeId(executionTask)
            .send()
    }

    fun sendFailure(e: RuntimeException) {
        messageHandler.builder()
            .payload(e.message)
            .destination(ctx)
            .status(ActionStatus.FAILURE)
            .nodeId(executionTask)
            .send()
    }

    fun sendSuccess() {
        messageHandler.builder()
            .destination(ctx)
            .status(ActionStatus.SUCCESS)
            .nodeId(executionTask)
            .send()
    }

    fun sendValidationFailed(e: TaskException) {
        messageHandler.builder()
            .payload(e.message)
            .destination(ctx)
            .status(ActionStatus.VALIDATION_FAILED)
            .nodeId(executionTask)
            .send()
    }

}
