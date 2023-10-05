package com.cn.tg.flooow.enums

import java.lang.reflect.Field

enum class OptionValueType {
    STRING,
    MAP,
    ENUM;

    companion object {
        fun parseFromField(field: Field): OptionValueType {
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
