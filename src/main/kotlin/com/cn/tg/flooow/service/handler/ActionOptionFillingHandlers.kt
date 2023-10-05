package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.service.ExecutionTask
import com.cn.tg.flooow.service.TaskContext
import com.cn.tg.flooow.utils.ReflectUtils
import org.springframework.stereotype.Component

@Component
class ActionOptionFillingHandlers(private val optionFillingHandler: DefaultActionOptionFillingHandler) {

    fun apply(ctx: TaskContext, executionTask: ExecutionTask) {
        val (task, action) = executionTask
        val optionName2Field = ReflectUtils.getDeclaredFieldWithAnnotation(action.javaClass, ActionOption::class.java)
            .let { it.associateBy { field -> field.getAnnotation(ActionOption::class.java).name } }
        optionFillingHandler.apply(ctx, action, task.options, optionName2Field)
    }

}

