package com.cn.tg.flooow.controller.response

class R(val message: String) {
    var code: Int = 0

    companion object {
        fun failed(message: String): R {
            return R(message)
        }
    }
}
