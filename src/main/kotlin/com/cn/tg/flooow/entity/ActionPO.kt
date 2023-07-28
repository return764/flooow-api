package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.enums.ActionStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.Instant


@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE actions SET deleted_at = now() WHERE id=?")
@Entity(name = "actions")
data class ActionPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,
    @Column(name = "template_id")
    val templateId: String?,
    @Column(name = "node_id")
    val nodeId: String,
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: ActionStatus,
    @Column(name = "value")
    val value: String,
    override var deletedAt: Instant? = null
): AuditingEntity()
