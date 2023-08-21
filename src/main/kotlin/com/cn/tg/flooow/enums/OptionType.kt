package com.cn.tg.flooow.enums

import java.lang.reflect.Field

enum class OptionType {
    STRING,
    MAP,
    ENUM;

    companion object {
        fun parseFromField(field: Field): OptionType {
            val type = field.type
            if (type.isEnum) {
                return ENUM
            }

            if (Map::class.java.isAssignableFrom(type)) {
                return MAP
            }
            return STRING
        }
    }
}
