package com.cn.tg.flooow.entity.vo

enum class OptionInputType {
    DEFAULT,
    LAST_INPUT,
}

data class ActionOptionVO(
    val id: String,
    val label: String,
    val type: String,
    val inputType: OptionInputType,
    val value: String
) {

}
