package com.cn.tg.flooow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "action_template_options")
data class ActionTemplateOptionPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,
    @Column(name = "template_id")
    val templateId: String,
    @Column(name = "key")
    val key: String,
    @Column(name = "default_value")
    val defaultValue: String,
    @Column(name = "type")
    val type: String,
    @Column(name = "visible")
    val visible: Boolean
)
