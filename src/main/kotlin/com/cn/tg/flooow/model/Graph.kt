package com.cn.tg.flooow.model

import com.cn.tg.flooow.model.action.Action
import java.util.LinkedList

class Graph {
    private val taskList = LinkedList<Action>()

    init {
        taskList.addAll(
            listOf(
            )
        )
    }
}
