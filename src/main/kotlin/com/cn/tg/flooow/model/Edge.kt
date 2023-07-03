package com.cn.tg.flooow.model


data class Edge(
    val id: String,
    val shape: String,
    val source: PortLinkPoint,
    val target: PortLinkPoint,
    val zIndex: Int? = -1,
) {
}

data class PortLinkPoint(
    val cell: String,
    val port: String
) {

}
