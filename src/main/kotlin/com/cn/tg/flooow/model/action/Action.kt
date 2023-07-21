package com.cn.tg.flooow.model.action

import com.cn.tg.flooow.service.TaskContext


interface Action {
    val ctx: TaskContext
    fun run()
    fun validate()

    fun bind(ctx: TaskContext): Action
}
