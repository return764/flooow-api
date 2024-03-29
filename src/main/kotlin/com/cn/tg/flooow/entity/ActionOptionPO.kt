package com.cn.tg.flooow.entity

import com.cn.tg.flooow.entity.base.AuditingEntity
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionType
import com.cn.tg.flooow.enums.OptionValueType
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Type
import org.hibernate.annotations.Where
import java.time.Instant


@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE action_options SET deleted_at = now() WHERE id=?")
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
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: OptionType,
    @Column(name = "value_type")
    @Enumerated(EnumType.STRING)
    val valueType: OptionValueType,
    @Type(JsonType::class)
    @Column(name = "json_type_value", columnDefinition = "jsonb")
    val typeAndValue: OptionTypeValue,
    @Column(name = "visible")
    val visible: Boolean,
    override var deletedAt: Instant? = null
): AuditingEntity() {

    fun toVO(): ActionOptionVO {
        return ActionOptionVO(
            id = id!!,
            label = key,
            valueType = valueType,
            value = typeAndValue.value,
        )
    }
}

data class OptionTypeValue(
    val type: String,
    val value: Any?
)
