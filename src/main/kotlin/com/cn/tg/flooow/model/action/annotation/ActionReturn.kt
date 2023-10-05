package com.cn.tg.flooow.model.action.annotation

import kotlin.reflect.KClass

annotation class ActionReturn(
    val name: String,
    val type: KClass<*>
)
