package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.request.GraphCreationRequest
import com.cn.tg.flooow.controller.request.UpdateActionOptionsRequest
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.service.GraphService
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.entity.vo.GraphSummaryVO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("graphs")
class GraphController(
    private val graphService: GraphService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createGraph(@RequestBody request: GraphCreationRequest): GraphSummaryVO {
        return graphService.createGraph(request)
    }

    @DeleteMapping("{id}")
    fun createGraph(@PathVariable id: String): GraphSummaryVO {
        return graphService.deleteGraph(id)
    }

    @GetMapping("{id}")
    fun getGraphData(@PathVariable id: String): GraphDataVO {
        return graphService.getGraphData(id)
    }

    @GetMapping
    fun getGraphList(): List<GraphSummaryVO> {
        return graphService.retrieveAllGraph()
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
