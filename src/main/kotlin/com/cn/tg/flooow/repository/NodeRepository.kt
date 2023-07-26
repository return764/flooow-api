package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.NodePO
import org.springframework.data.jpa.repository.JpaRepository

interface NodeRepository: JpaRepository<NodePO, String> {
    fun findAllByGraphId(graphId: String): List<NodePO>
}
