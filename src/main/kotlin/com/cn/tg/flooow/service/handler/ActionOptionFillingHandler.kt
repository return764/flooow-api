package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.OptionInputType
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.service.TaskContext
import java.lang.reflect.Field

interface ActionOptionFillingHandler {

    fun apply(
        ctx: TaskContext,
        action: Action,
        options: List<ActionOptionVO>,
        optionName2Field: Map<String, Field>
    )

    fun getOptionInputType(): OptionInputType
}
