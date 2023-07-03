package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.ActionPO
import org.springframework.data.jpa.repository.JpaRepository

interface ActionRepository: JpaRepository<ActionPO, String> {
}
