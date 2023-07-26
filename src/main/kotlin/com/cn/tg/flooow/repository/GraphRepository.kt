package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.GraphPO
import org.springframework.data.jpa.repository.JpaRepository

interface GraphRepository: JpaRepository<GraphPO, String> {

}
