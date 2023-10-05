package com.cn.tg.flooow.enums

import com.cn.tg.flooow.model.action.template.enums.HttpMethod
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaField

class OptionValueTypeTest {

    private val testMap: Map<String, String> = HashMap()
    private val testString: String = ""
    private val testEnum: HttpMethod = HttpMethod.GET

    @Test
    fun test_parse_field_type() {
        val expectMap = OptionValueType.parseFromField(this::testMap.javaField!!)
        val expectString = OptionValueType.parseFromField(this::testString.javaField!!)
        val expectEnum = OptionValueType.parseFromField(this::testEnum.javaField!!)

        assertEquals(expectMap, OptionValueType.MAP)
        assertEquals(expectString, OptionValueType.STRING)
        assertEquals(expectEnum, OptionValueType.ENUM)
    }
}
