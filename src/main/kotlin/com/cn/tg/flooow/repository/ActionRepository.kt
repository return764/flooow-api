package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.ActionPO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActionRepository: JpaRepository<ActionPO, String> {
    fun findByNodeId(nodeId: String): Optional<ActionPO>
}
