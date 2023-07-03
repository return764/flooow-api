package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.ActionOptionPO
import org.springframework.data.jpa.repository.JpaRepository

interface ActionOptionRepository: JpaRepository<ActionOptionPO, String> {

    fun findAllByNodeId(nodeId: String): List<ActionOptionPO>
    fun findAllByActionId(id: String): List<ActionOptionPO>
}
