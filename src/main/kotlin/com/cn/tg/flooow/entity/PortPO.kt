package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.model.PortMeta
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.Instant


@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE nodes_ports SET deleted_at = now() WHERE id=?")
@Entity(name = "nodes_ports")
data class PortPO(
    @Id
    @Column(name = "id")
    val id: String,
    @Column(name = "node_id")
    val nodeId: String,
    @Column(name = "`group`")
    val group: String,
    override var deletedAt: Instant? = null
): AuditingEntity() {
    fun toModel(): PortMeta {
        return PortMeta(
            id = id,
            group = group,
        )
    }
}
