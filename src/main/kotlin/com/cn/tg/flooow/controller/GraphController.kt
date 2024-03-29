package com.cn.tg.flooow.controller

import com.cn.tg.flooow.controller.request.GraphCreationRequest
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.entity.vo.GraphSummaryVO
import com.cn.tg.flooow.enums.ReturnType
import com.cn.tg.flooow.service.GraphExecutionService
import com.cn.tg.flooow.service.GraphService
import com.cn.tg.flooow.service.MessageHandler
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
    private val graphService: GraphService,
    private val graphExecutionService: GraphExecutionService,
    private val template: MessageHandler,
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

    @GetMapping("templates")
    fun retrieveTemplates(): List<ActionTemplateVO> {
        return graphService.retrieveAllTemplates()
    }

    @PostMapping("{graphId}/execution")
    fun executeGraph(@PathVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphExecutionService.execute(graphId))
            .returnType(ReturnType.EXECUTION)
            .send()
    }

}
