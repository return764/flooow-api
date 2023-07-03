package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.EdgePO
import org.springframework.data.jpa.repository.JpaRepository

interface EdgeRepository: JpaRepository<EdgePO, String> {
}
