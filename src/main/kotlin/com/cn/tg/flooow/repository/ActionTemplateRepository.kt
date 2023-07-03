package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.ActionTemplatePO
import org.springframework.data.jpa.repository.JpaRepository

interface ActionTemplateRepository: JpaRepository<ActionTemplatePO, String> {
    fun findByTemplateName(name: String?): ActionTemplatePO?
}
