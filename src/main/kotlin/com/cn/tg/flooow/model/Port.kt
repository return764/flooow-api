package com.cn.tg.flooow.model

import com.cn.tg.flooow.entity.PortPO

data class Port(
    val items: List<PortMeta>
)

data class PortMeta(
    val id: String,
    val group: String,
) {
    fun toPO(nodeId: String): PortPO {
        return PortPO(
            id = id,
            nodeId = nodeId,
            group = group,
        )
    }
}
