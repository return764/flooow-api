package com.cn.tg.flooow.model.action

import com.cn.tg.flooow.service.TaskContext


interface Action {
    val ctx: TaskContext
    fun run(ctx: TaskContext)

    fun bind(ctx: TaskContext): Action
}
