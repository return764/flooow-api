package com.cn.tg.flooow.model.task

import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.ActionChains
import com.rotten.flowEbackend.model.task.TaskExecutor

class Task : Action {
    private val actions: TaskExecutor = TaskExecutor()
    override fun run(actionChains: ActionChains, arg: String): String {
        TODO("Not yet implemented")
    }

}
