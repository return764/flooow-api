package com.cn.tg.flooow.enums

import java.lang.reflect.Field

enum class OptionValueType {
    STRING,
    MAP,
    ENUM;

    companion object {
        fun parseFromField(field: Field): OptionValueType {
            return parse(field.type)
        }

        fun parse(clazz: Class<*>): OptionValueType {
            if (clazz.isEnum) {
                return ENUM
            }

            if (Map::class.java.isAssignableFrom(clazz)) {
                return MAP
            }
            return STRING
        }
    }
}
