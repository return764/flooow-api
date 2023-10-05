package com.cn.tg.flooow.model.action.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionMarker(
    val parent: String = "",
    val type: String,
    val shape: String,
    val name: String,
    val label: String,
)
