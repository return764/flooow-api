package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.request.UpdateActionOptionsRequest
import com.cn.tg.flooow.controller.response.Option
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.enums.OptionType
import com.cn.tg.flooow.service.GraphService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("nodes")
class NodeController(
    private val graphService: GraphService,
) {

    @GetMapping("{nodeId}/options")
    fun getActionOptions(@PathVariable nodeId: String): List<ActionOptionVO>{
        return graphService.getActionOptions(nodeId)
    }

    @PostMapping("{nodeId}/options")
    fun updateActionOptions(@PathVariable nodeId: String,
                            @RequestBody request: UpdateActionOptionsRequest
    ): List<ActionOptionVO>{
        return graphService.updateActionOptions(nodeId, request.data)
    }

    @GetMapping("enumOptions")
    fun getEnumOptions(id: String): List<Option>{
        val optionPO = graphService.getActionOptionById(id)
        if (OptionType.ENUM != optionPO.type) {
            throw RuntimeException("this option type is not ENUM.")
        }
        try {
            val clazz = Class.forName(optionPO.typeValue.type)

            if (!clazz.isEnum) {
                throw RuntimeException("type is not support.")
            }

            return clazz.enumConstants.map {
                Option(it.toString(), it.toString())
            }
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("type is invalid.")
        }
    }
}
