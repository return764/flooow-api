package com.cn.tg.flooow.entity.vo

import com.cn.tg.flooow.enums.OptionValueType

data class ActionOptionVO(
    val id: String,
    val label: String,
    val valueType: OptionValueType,
    val value: Any?
) {

}
