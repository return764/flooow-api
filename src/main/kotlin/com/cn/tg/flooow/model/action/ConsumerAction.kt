package com.cn.tg.flooow.model.action

interface ConsumerAction : Action {
    fun run(arg: String)
}
