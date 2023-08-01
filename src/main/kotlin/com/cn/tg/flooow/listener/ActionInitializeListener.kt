package com.cn.tg.flooow.listener

import com.cn.tg.flooow.entity.ActionTemplateOptionPO
import com.cn.tg.flooow.entity.ActionTemplatePO
import com.cn.tg.flooow.model.action.Action
import com.cn.tg.flooow.model.action.annotation.ActionMarker
import com.cn.tg.flooow.model.action.annotation.ActionOption
import com.cn.tg.flooow.repository.ActionTemplateOptionRepository
import com.cn.tg.flooow.repository.ActionTemplateRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(0)
@Component
class ActionInitializeListener(
    private val actionTemplateRepository: ActionTemplateRepository,
    private val actionTemplateOptionRepository: ActionTemplateOptionRepository,
    private val actionTemplates: List<Action>
): ApplicationListener<ApplicationReadyEvent> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        actionTemplates
            .filter { it.javaClass.isAnnotationPresent(ActionMarker::class.java) }
            .forEach {
                val actionMarker = it.javaClass.getAnnotation(ActionMarker::class.java)
                val template = actionTemplateRepository.findByTemplateName(actionMarker.name)
                if (template == null) {
                    val newTemplate = actionTemplateRepository.save(ActionTemplatePO(
                        templateName = actionMarker.name,
                        className = it.javaClass.name,
                        type = actionMarker.type,
                        shape = actionMarker.shape,
                        parent = actionMarker.parent.ifEmpty { null }
                    ))
                    storeActionOptions(it, newTemplate, actionMarker)

                } else {
                    actionTemplateOptionRepository.deleteAllByTemplateId(template.id)
                    storeActionOptions(it, template, actionMarker)
                }
                logger.info("Load action template [${actionMarker.name}] successful")

            }
    }

    private fun storeActionOptions(
        it: Action,
        newTemplate: ActionTemplatePO,
        actionMarker: ActionMarker
    ) {
        getActionOptionFields(it)
            .map { field ->
                val option = field.getAnnotation(ActionOption::class.java)
                ActionTemplateOptionPO(
                    templateId = newTemplate.id!!,
                    type = field.type.typeName,
                    key = option.name,
                    defaultValue = option.defaultValue,
                    visible = true
                )
            }.also { list ->
                actionTemplateOptionRepository.deleteAllByTemplateId(newTemplate.id)
                actionTemplateOptionRepository.saveAll(list)
                actionTemplateOptionRepository.saveAll(otherOptions(newTemplate, actionMarker))
            }
    }

    private fun getActionOptionFields(it: Action) = it.javaClass.declaredFields
        .filter { item -> item.isAnnotationPresent(ActionOption::class.java) }

    private fun otherOptions(
        template: ActionTemplatePO,
        actionMarker: ActionMarker
    ): List<ActionTemplateOptionPO> {
        return listOf(
            ActionTemplateOptionPO(
                templateId = template.id!!,
                type = "java.lang.String",
                key = "label",
                defaultValue = actionMarker.label,
                visible = false
            ),
            ActionTemplateOptionPO(
                templateId = template.id,
                type = "java.lang.String",
                key = "template",
                defaultValue = actionMarker.name,
                visible = false
            ),
        )
    }
}
