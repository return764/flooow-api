package com.cn.tg.flooow.entity.vo

import com.cn.tg.flooow.enums.OptionInputType
import com.cn.tg.flooow.enums.OptionType

data class ActionOptionVO(
    val id: String,
    val label: String,
    val type: OptionType,
    val inputType: OptionInputType,
    val value: Any?
) {

}
