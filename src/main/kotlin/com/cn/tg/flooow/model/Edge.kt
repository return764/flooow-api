package com.cn.tg.flooow.model

import com.cn.tg.flooow.entity.EdgePO
import com.fasterxml.jackson.annotation.JsonProperty


data class Edge(
    val id: String,
    val shape: String,
    val source: PortLinkPoint,
    val target: PortLinkPoint,
    @field:JsonProperty("zIndex")
    val zIndex: Int? = -1,
) {
    fun toPO(graphId: String) = EdgePO(
        id = id,
        shape = shape,
        graphId = graphId,
        sourceCellId = source.cell,
        sourcePortId = source.port,
        targetCellId = target.cell,
        targetPortId = target.port,
    )
}

data class PortLinkPoint(
    val cell: String,
    val port: String
) {

}
