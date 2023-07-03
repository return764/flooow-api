package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.ActionTemplateOptionPO
import org.springframework.data.jpa.repository.JpaRepository

interface ActionTemplateOptionRepository: JpaRepository<ActionTemplateOptionPO, String> {
    fun findAllByTemplateId(id: String?): List<ActionTemplateOptionPO>

    fun deleteAllByTemplateId(id: String?)
}
