package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.model.PortLinkPoint
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.Instant

@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE nodes_edges SET deleted_at = now() WHERE id=?")
@Entity(name = "nodes_edges")
data class EdgePO(
    @Id
    @Column(name = "id")
    val id: String,
    @Column(name = "graph_id")
    val graphId: String,
    @Column(name = "shape")
    val shape: String,
    @Column(name = "source_cell_id")
    val sourceCellId: String,
    @Column(name = "source_port_id")
    val sourcePortId: String,
    @Column(name = "target_cell_id")
    val targetCellId: String,
    @Column(name = "target_port_id")
    val targetPortId: String,
    override var deletedAt: Instant? = null
): AuditingEntity() {
    fun toModel(): Edge {
        return Edge(
            id = id,
            shape = shape,
            source = PortLinkPoint(
                cell = sourceCellId,
                port = sourcePortId,
            ),
            target = PortLinkPoint(
                cell = targetCellId,
                port = targetPortId
            )
        )
    }
}
