package com.cn.tg.flooow.entity.vo

import com.cn.tg.flooow.enums.OptionInputType

data class ActionOptionVO(
    val id: String,
    val label: String,
    val type: String,
    val inputType: OptionInputType,
    val value: Any?
) {

}
