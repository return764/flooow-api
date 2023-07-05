package com.cn.tg.flooow.model.dag

import java.util.*

class DirectedAcyclicGraph<Node> {
    // 所有入度为0的节点，即为开始节点
    // 所有出度为0的节点，即为结束节点
    // 环校验

    private val graph: MutableMap<Node, MutableList<Node>> = mutableMapOf()
    private val reverseGraph: MutableMap<Node, MutableList<Node>> = mutableMapOf()

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

}
