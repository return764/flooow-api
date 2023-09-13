package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionInputType
import com.cn.tg.flooow.enums.OptionType
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
            if (it.type == OptionType.ENUM) {
                val enumConstants = field.type.enumConstants
                field.set(action, enumConstants.first { enum -> (enum as Enum<*>).name == it.value })
            } else if (it.type == OptionType.MAP) {
                field.set(action, it.value)
            } else {
                field.set(action, it.value)
            }
        }
    }

    override fun getOptionInputType(): OptionInputType {
        return OptionInputType.DEFAULT
    }
}
