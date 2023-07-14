package com.cn.tg.flooow.controller.request

import com.cn.tg.flooow.entity.vo.ActionOptionVO

data class UpdateActionOptionsRequest(
    val data: List<ActionOptionVO>
) {
}
