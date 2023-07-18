package com.cn.tg.flooow.service.handler

import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.service.ExecutionTask
import com.cn.tg.flooow.service.TaskContext
import com.cn.tg.flooow.utils.ReflectUtils
import org.springframework.stereotype.Component

@Component
class ActionOptionFillingHandlers(private val optionFillingHandlers: List<ActionOptionFillingHandler>) {

    private val handlerMap by lazy { optionFillingHandlers.associateBy { it.getOptionInputType() } }

    fun apply(ctx: TaskContext, executionTask: ExecutionTask) {
        val (task, action) = executionTask
        val optionName2Field = ReflectUtils.getDeclaredFieldWithAnnotation(action.javaClass, ActionOption::class.java)
            .let { it.associateBy { field -> field.getAnnotation(ActionOption::class.java).name } }
        task.options
            .groupBy { it.inputType }
            .forEach {
                handlerMap[it.key]?.apply(ctx, action, it.value, optionName2Field)
            }
    }

}
