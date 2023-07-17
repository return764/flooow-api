package com.cn.tg.flooow.model

import com.cn.tg.flooow.entity.ActionPO
import com.cn.tg.flooow.entity.ActionTemplatePO
import com.cn.tg.flooow.entity.NodePO
import com.cn.tg.flooow.entity.PortPO
import com.cn.tg.flooow.model.action.ActionStatus

data class Node(
    val id: String,
    val shape: String,
    val name: String?,
    val x: Int,
    val y: Int,
    val height: Int?,
    val width: Int?,
    val ports: Port,
    val data: Map<String, String>,
) {
    fun toPO(label: String?): NodePO {
        return NodePO(
            id = id,
            name = name ?: generateName(label),
            shape = shape,
            x = x,
            y = y,
            height = height,
            width = width
        )
    }

    private fun generateName(label: String?): String {
        return "$label-${id.slice(0..4)}"
    }

    fun toActionPO(template: ActionTemplatePO?): ActionPO {
        return ActionPO(
            templateId = template?.id,
            nodeId = id,
            status = ActionStatus.NEW,
            value = data["value"] ?: ""
        )
    }

    fun buildPortsPOs(): List<PortPO> {
        return this.ports.items.map {
            it.toPO(this.id)
        }
    }
}
