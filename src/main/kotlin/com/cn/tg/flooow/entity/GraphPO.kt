package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.entity.vo.GraphSummaryVO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "graphs")
data class GraphPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "name")
    val name: String,
    override var deletedAt: Instant? = null
): AuditingEntity() {
    fun toSummary(): GraphSummaryVO {
        return GraphSummaryVO(id!!, name)
    }
}
