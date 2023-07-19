package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionInputType
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.service.TaskContext
import org.springframework.stereotype.Component
import java.lang.reflect.Field

@Component
class DefaultActionOptionFillingHandler: ActionOptionFillingHandler {

    override fun apply(
        ctx: TaskContext,
        action: Action,
        options: List<ActionOptionVO>,
        optionName2Field: Map<String, Field>
    ) {
        options.forEach {
            optionName2Field[it.label]?.trySetAccessible()
            optionName2Field[it.label]?.set(action, it.value)
        }
    }

    override fun getOptionInputType(): OptionInputType {
        return OptionInputType.DEFAULT
    }
}
