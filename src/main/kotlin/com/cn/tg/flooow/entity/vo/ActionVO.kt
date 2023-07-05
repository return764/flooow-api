package com.cn.tg.flooow.entity.vo

import com.cn.tg.flooow.model.action.ActionStatus

data class ActionVO(
    val id: String,
    val templateName: String,
    val className: String,
    val status: ActionStatus,
    val value: String,
)
