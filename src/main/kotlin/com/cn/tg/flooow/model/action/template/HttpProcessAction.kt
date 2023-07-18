package com.cn.tg.flooow.model.action.template

import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.service.TaskContext


@ActionMarker(type = "process", name = "http", shape = "process", label="HTTP Request")
class HttpProcessAction: Action {

    @ActionOption(name = "method", defaultValue = "GET")
    private lateinit var method: String

    @ActionOption(name = "url", defaultValue = "")
    private lateinit var url: String

    override fun run(ctx: TaskContext) {
        println("HTTP Request: $method $url")
    }
}
