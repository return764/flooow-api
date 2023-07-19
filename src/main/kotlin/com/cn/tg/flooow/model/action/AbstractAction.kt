package com.cn.tg.flooow.model.action

import com.cn.tg.flooow.service.ExecutionTask
import com.cn.tg.flooow.service.TaskContext

abstract class AbstractAction : Action {
    override lateinit var ctx: TaskContext

    override fun bind(ctx: TaskContext): Action {
        this.ctx = ctx
        return this
    }

    fun current(): ExecutionTask {
        return ctx.currentTask(this)
    }
}
