package com.cn.tg.flooow.model.action


interface Action {
    fun run(actionChains: ActionChains, arg: String): String
}
