package com.cn.tg.flooow.entity.vo

import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.model.Node

data class GraphDataVO(
    val nodes: List<Node>,
    val edges: List<Edge>)
