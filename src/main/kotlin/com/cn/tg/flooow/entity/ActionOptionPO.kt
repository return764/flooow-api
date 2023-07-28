package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionInputType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

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
    @Column(name = "input_type")
    @Enumerated(EnumType.STRING)
    val inputType: OptionInputType,
    @Column(name = "value")
    val value: String,
    @Column(name = "type")
    val type: String,
    @Column(name = "visible")
    val visible: Boolean,
    override var deletedAt: Instant? = null
): AuditingEntity() {
    fun toVO(): ActionOptionVO {
        return ActionOptionVO(
            id = id!!,
            label = key,
            inputType = inputType,
            type = type,
            value = value
        )
    }
}
