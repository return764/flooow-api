package com.cn.tg.flooow.entity

import com.cn.tg.flooow.model.PortMeta
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "nodes_ports")
data class PortPO(
    @Id
    @Column(name = "id")
    val id: String,
    @Column(name = "node_id")
    val nodeId: String,
    @Column(name = "`group`")
    val group: String,
    @Column(name = "is_deleted")
    val isDeleted: Boolean = false
) {
    fun toModel(): PortMeta {
        return PortMeta(
            id = id,
            group = group,
        )
    }
}
