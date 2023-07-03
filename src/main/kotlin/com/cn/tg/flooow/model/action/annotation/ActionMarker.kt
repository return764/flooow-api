package com.cn.tg.flooow.model.action.annotation

import org.springframework.stereotype.Component


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class ActionMarker(
    val parent: String = "",
    val type: String,
    val shape: String,
    val name: String,
    val label: String,
)
