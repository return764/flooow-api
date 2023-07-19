package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker


@ActionMarker(name = "console", type = "customer", shape = "output", label = "Console Print")
class ConsoleCustomerAction:AbstractAction(), Action {

    override fun run() {
        println("console something ${ctx.receiveValue(this).values}")
    }
}
