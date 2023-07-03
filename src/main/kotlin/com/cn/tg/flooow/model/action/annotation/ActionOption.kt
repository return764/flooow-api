package com.cn.tg.flooow.model.action.annotation

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionOption(
    val name: String,
    val defaultValue: String
)
