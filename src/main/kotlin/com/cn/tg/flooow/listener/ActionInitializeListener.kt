package com.cn.tg.flooow.listener

import com.cn.tg.flooow.entity.ActionTemplateOptionPO
import com.cn.tg.flooow.entity.ActionTemplatePO
import com.cn.tg.flooow.entity.OptionTypeValue
import com.cn.tg.flooow.enums.OptionType
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf


@Order(0)
@Component
class ActionInitializeListener(
    private val actionTemplateRepository: ActionTemplateRepository,
    private val actionTemplateOptionRepository: ActionTemplateOptionRepository
): ApplicationListener<ApplicationReadyEvent> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        scanTemplates()
            .forEach {
                val actionMarker = it.java.getAnnotation(ActionMarker::class.java)
                val template = actionTemplateRepository.findByTemplateName(actionMarker.name)
                if (template == null) {
                    val newTemplate = actionTemplateRepository.save(ActionTemplatePO(
                        templateName = actionMarker.name,
                        className = it.java.name,
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

    private fun scanTemplates(): MutableList<KClass<Action>> {
        val actionTemplateClasses = mutableListOf<KClass<Action>>()

        val resolver = PathMatchingResourcePatternResolver()
        val pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath("com.cn.tg.flooow.model.action.template") +
                "/*.class"
        val resources = resolver.getResources(pattern)
        for (resource in resources) {
            val scannedClassName: String = SimpleMetadataReaderFactory()
                .getMetadataReader(resource)
                .classMetadata
                .className
            val scannedClass = Class.forName(scannedClassName).kotlin
            if (scannedClass.isSubclassOf(Action::class)
                && scannedClass.hasAnnotation<ActionMarker>()
            ) {
                actionTemplateClasses.add(scannedClass as KClass<Action>)
            }
        }
        return actionTemplateClasses
    }

    private fun storeActionOptions(
        it: KClass<Action>,
        newTemplate: ActionTemplatePO,
        actionMarker: ActionMarker
    ) {
        getActionOptionFields(it)
            .map { field ->
                val option = field.getAnnotation(ActionOption::class.java)
                ActionTemplateOptionPO(
                    templateId = newTemplate.id!!,
                    key = option.name,
                    type = OptionType.parseFromField(field),
                    defaultTypeValue = OptionTypeValue(field.type.typeName, option.defaultValue.ifBlank { null }),
                    visible = true
                )
            }.also { list ->
                actionTemplateOptionRepository.deleteAllByTemplateId(newTemplate.id)
                actionTemplateOptionRepository.saveAll(list)
                actionTemplateOptionRepository.saveAll(otherOptions(newTemplate, actionMarker))
            }
    }

    private fun getActionOptionFields(it: KClass<Action>) = it.java.declaredFields
        .filter { item -> item.isAnnotationPresent(ActionOption::class.java) }

    private fun otherOptions(
        template: ActionTemplatePO,
        actionMarker: ActionMarker
    ): List<ActionTemplateOptionPO> {
        return listOf(
            ActionTemplateOptionPO(
                templateId = template.id!!,
                key = "label",
                defaultTypeValue = OptionTypeValue("java.lang.String",actionMarker.label),
                type = OptionType.STRING,
                visible = false
            ),
            ActionTemplateOptionPO(
                templateId = template.id,
                key = "template",
                defaultTypeValue = OptionTypeValue("java.lang.String",actionMarker.name),
                type = OptionType.STRING,
                visible = false
            ),
        )
    }
}
