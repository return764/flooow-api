package com.cn.tg.flooow.model

import com.fasterxml.jackson.annotation.JsonProperty


data class Edge(
    val id: String,
    val shape: String,
    val source: PortLinkPoint,
    val target: PortLinkPoint,
    @field:JsonProperty("zIndex")
    val zIndex: Int? = -1,
) {
}

data class PortLinkPoint(
    val cell: String,
    val port: String
) {

}
