package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.vo.GraphSummaryVO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "graphs")
data class GraphPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "name")
    val name: String,
    @Column(name = "is_deleted")
    val isDeleted: Boolean
) {
    fun toSummary(): GraphSummaryVO {
        return GraphSummaryVO(id!!, name)
    }
}
