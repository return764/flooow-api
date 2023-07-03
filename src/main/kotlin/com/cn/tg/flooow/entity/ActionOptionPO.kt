package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.vo.NodeOptionVO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "action_options")
data class ActionOptionPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,
    @Column(name = "action_id")
    val actionId: String,
    @Column(name = "node_id")
    val nodeId: String,
    @Column(name = "key")
    val key: String,
    @Column(name = "value")
    val value: String,
    @Column(name = "type")
    val type: String,
    @Column(name = "visible")
    val visible: Boolean,
    @Column(name = "is_deleted")
    val isDeleted: Boolean = false
) {
    fun toVO(): NodeOptionVO {
        return NodeOptionVO(
            label = key,
            type = type,
            value = value
        )
    }
}