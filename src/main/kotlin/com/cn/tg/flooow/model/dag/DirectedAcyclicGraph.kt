package com.cn.tg.flooow.model.dag

import java.util.*

class DirectedAcyclicGraph<Node> {
    private val graph: MutableMap<Node, MutableList<Node>> = mutableMapOf()
    private val reverseGraph: MutableMap<Node, MutableList<Node>> = mutableMapOf()

    // 环检查
    fun circleCheck(): Boolean {
        // too hard
        return true
    }

    // 孤立节点检查
    fun isolatedCheck(): Boolean {
        if (getLasts().intersect(getFirsts().toSet()).isNotEmpty()) {
            return false
        }
        return true
    }

    fun addNode(node: Node) {
        if (graph[node] == null) {
            graph[node] = mutableListOf()
        }
        if (reverseGraph[node] == null) {
            reverseGraph[node] = mutableListOf()
        }
    }

    fun addEdge(from: Node, to: Node) {
        addNode(from)
        addNode(to)
        if (!graph[from]?.contains(to)!!) {
            graph[from]?.add(to)
        }
        if (!reverseGraph[to]?.contains(from)!!) {
            reverseGraph[to]?.add(from)
        }
    }

    fun next(node: Node): Optional<List<Node>> {
        return Optional.ofNullable(graph[node])
    }

    fun previous(node: Node): Optional<List<Node>> {
        return Optional.ofNullable(reverseGraph[node])
    }

    fun getFirsts(): List<Node> {
        return reverseGraph.filter {
            it.value.isEmpty()
        }.keys.toList()
    }

    fun getLasts(): List<Node> {
        return graph.filter {
            it.value.isEmpty()
        }.keys.toList()
    }

    fun tasks(): List<Node> {
        return graph.keys.toList()
    }

}
