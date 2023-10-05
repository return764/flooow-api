package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.service.TaskContext
import java.lang.reflect.Field

class LastOutputActionOptionFillingHandler: ActionOptionFillingHandler {

    override fun apply(
        ctx: TaskContext,
        action: Action,
        options: List<ActionOptionVO>,
        optionName2Field: Map<String, Field>
    ) {
        val runtimeInputMap = ctx.receiveValue(action).map { item -> item.key.task.node.id to item.value }.toMap()
        options.forEach {
            optionName2Field[it.label]?.trySetAccessible()
            optionName2Field[it.label]?.set(action, runtimeInputMap[it.value])
        }
    }
}
