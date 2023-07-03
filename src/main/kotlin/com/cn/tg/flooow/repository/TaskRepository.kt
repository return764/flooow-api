package com.cn.tg.flooow.repository

import com.cn.tg.flooow.entity.TaskPO
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository: JpaRepository<TaskPO, String> {
}
