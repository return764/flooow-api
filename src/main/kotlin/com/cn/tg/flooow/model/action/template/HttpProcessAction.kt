package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.AbstractAction
import com.cn.tg.flooow.model.action.ActionChains
import com.cn.tg.flooow.model.action.ProcessAction
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption


@ActionMarker(type = "process", name = "http", shape = "process", label="HTTP Request")
class HttpProcessAction: AbstractAction(), ProcessAction {

    @ActionOption(name = "method", defaultValue = "GET")
    private lateinit var method: String


    override fun run(actionChains: ActionChains, arg: String): String {
        println("noting")
        return "ok"
    }
}
