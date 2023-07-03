package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.PortPO
import org.springframework.data.jpa.repository.JpaRepository

interface PortRepository: JpaRepository<PortPO, String> {
    fun findAllByNodeId(nodeId: String): List<PortPO>
}
