package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionValueType
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
            val field = optionName2Field[it.label]!!
            field.trySetAccessible()
            if (it.valueType == OptionValueType.ENUM) {
                val enumConstants = field.type.enumConstants
                field.set(action, enumConstants.first { enum -> (enum as Enum<*>).name == it.value })
            } else if (it.valueType == OptionValueType.MAP) {
                field.set(action, it.value)
            } else {
                field.set(action, it.value)
            }
        }
    }
}
