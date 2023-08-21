package com.cn.tg.flooow.entity

import com.cn.tg.flooow.enums.OptionType
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.Type

@Entity(name = "action_template_options")
data class ActionTemplateOptionPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,
    @Column(name = "template_id")
    val templateId: String,
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: OptionType,
    @Column(name = "key")
    val key: String,
    @Type(JsonType::class)
    @Column(name = "default_type_value", columnDefinition = "jsonb")
    val defaultTypeValue: OptionTypeValue,
    @Column(name = "visible")
    val visible: Boolean
)
