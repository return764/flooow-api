package com.cn.tg.flooow.model.action.annotation

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionReturns(
    val value: Array<ActionReturn>,
)
