package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.service.TaskContext


@ActionMarker(name = "console", type = "customer", shape = "output", label = "Console Print")
class ConsoleCustomerAction:AbstractAction(), Action {

    override fun run(ctx: TaskContext) {
        println("console something ${ctx.receiveValue(this).values}")
    }
}
