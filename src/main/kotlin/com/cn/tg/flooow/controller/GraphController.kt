package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.request.UpdateActionOptionsRequest
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.service.GraphService
import com.cn.tg.flooow.entity.vo.GraphDataVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("graph")
class GraphController(
    private val graphService: GraphService
) {

    @PostMapping("execute")
    fun execute(): String {
        return "11"
    }

    @GetMapping
    fun getGraphData(): GraphDataVO {
        return graphService.getGraphData("mock-id")
    }

    @GetMapping("node/{nodeId}/options")
    fun getActionOptions(@PathVariable nodeId: String): List<ActionOptionVO>{
        return graphService.getActionOptions(nodeId)
    }

    @PostMapping("node/{nodeId}/options")
    fun updateActionOptions(@PathVariable nodeId: String,
                            @RequestBody request: UpdateActionOptionsRequest): List<ActionOptionVO>{
        return graphService.updateActionOptions(nodeId, request.data)
    }

    @GetMapping("templates")
    fun retrieveTemplates(): List<ActionTemplateVO> {
        return graphService.retrieveAllTemplates()
    }

}
