package com.rotten.flowEbackend.model.task

import com.cn.tg.flooow.model.action.Action

typealias Stack<E> = ArrayDeque<E>

fun <E> Stack<E>.push(item: E) = addLast(item)
fun <E> Stack<E>.peek(): E = last()
fun <E> Stack<E>.pop(): E = removeLast()


class ActionContainer {
    val Actions: Set<Stack<Action>> = HashSet()

    fun register(Action: Action) {

    }
}

class TaskExecutor {
    val ActionMap : Map<String, Action> = HashMap()


    fun run() {

    }
}
