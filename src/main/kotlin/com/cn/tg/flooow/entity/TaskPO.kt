package com.cn.tg.flooow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity(name = "tasks")
data class TaskPO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String? = null,
    @Column(name = "name")
    val name: String,
    @OneToMany
    @Column(name = "extends")
    val actions: List<ActionPO>,
    @Column(name = "delegate_action_id")
    val delegateActionId: String,
)
