package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.request.UpdateActionOptionsRequest
import com.cn.tg.flooow.entity.vo.ActionOptionVO
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
}
