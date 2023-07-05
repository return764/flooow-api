package com.cn.tg.flooow.model.action

import com.cn.tg.flooow.service.TaskContext


interface Action {
    fun run(ctx: TaskContext)
}
