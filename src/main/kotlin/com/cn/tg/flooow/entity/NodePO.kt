package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.model.Port
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.Instant


@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE nodes SET deleted_at = now() WHERE id=?")
@Entity(name = "nodes")
data class NodePO (
    @Id
    @Column(name = "id")
    val id: String,
    @Column(name = "graph_id")
    val graphId: String,
    @Column(name = "shape")
    val shape: String,
    @Column(name = "name")
    val name: String,
    @Column(name = "x")
    val x: Int,
    @Column(name = "y")
    val y: Int,
    @Column(name = "width")
    val width: Int?,
    @Column(name = "height")
    val height: Int?,
    override var deletedAt: Instant? = null
): AuditingEntity() {
    fun toModel(ports: List<PortPO>, options: List<ActionOptionPO>): Node {
        return Node(
            id = id,
            shape = shape,
            name = name,
            x = x,
            y = y,
            width = width,
            height = height,
            ports = Port(items = ports.map { it.toModel() }),
            data = options.associate { it.key to it.typeAndValue.value }
        )
    }
}
