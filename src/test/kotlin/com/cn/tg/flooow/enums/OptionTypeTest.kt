package com.cn.tg.flooow.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaField

class OptionTypeTest {

    private val testMap: Map<String, String> = HashMap()
    private val testString: String = ""
    private val testEnum: OptionInputType = OptionInputType.DEFAULT

    @Test
    fun test_parse_field_type() {
        val expectMap = OptionType.parseFromField(this::testMap.javaField!!)
        val expectString = OptionType.parseFromField(this::testString.javaField!!)
        val expectEnum = OptionType.parseFromField(this::testEnum.javaField!!)

        assertEquals(expectMap, OptionType.MAP)
        assertEquals(expectString, OptionType.STRING)
        assertEquals(expectEnum, OptionType.ENUM)
    }
}
