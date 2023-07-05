package com.cn.tg.flooow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "action_templates")
data class ActionTemplatePO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,

    @Column(name = "class_name")
    val className: String,

    @Column(name = "template_name")
    val templateName: String,

    @Column(name = "type")
    val type: String,

    @Column(name = "shape")
    val shape: String,

    @Column(name = "parent")
    val parent: String?,
)
