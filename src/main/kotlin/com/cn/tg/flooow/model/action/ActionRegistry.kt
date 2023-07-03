package com.cn.tg.flooow.model.action

import com.cn.tg.flooow.model.action.annotation.ActionMarker
import org.springframework.stereotype.Component

@Component
class ActionRegistry(private val actions: List<Action>) {
    val internalActionMap = mutableMapOf<String, Action>()

    init {
        actions.forEach {
            if (it::class.java.isAnnotationPresent(ActionMarker::class.java)) {
                println(it::javaClass.name)
            }
        }
    }
}
