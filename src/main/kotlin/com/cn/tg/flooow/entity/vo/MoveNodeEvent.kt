package com.cn.tg.flooow.entity.vo

data class MoveNodeEvent(
    val id: String,
    val previousX: Int,
    val previousY: Int,
    val postX: Int,
    val postY: Int,
)
