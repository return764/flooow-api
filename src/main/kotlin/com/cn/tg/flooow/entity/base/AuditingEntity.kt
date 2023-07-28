package com.cn.tg.flooow.entity.base

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant


@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class AuditingEntity(open var deletedAt: Instant? = null) {
    @CreatedDate
    lateinit var createdAt: Instant
    @LastModifiedDate
    lateinit var updatedAt: Instant

    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}
