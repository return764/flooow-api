package com.cn.tg.flooow.controller

import com.cn.tg.flooow.entity.vo.NodeOptionVO
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.service.GraphService
import com.cn.tg.flooow.entity.vo.GraphDataVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("graph")
class GraphController(
    private val graphService: GraphService
) {

    @PostMapping("execute")
    fun execute(): String {
        graphService.executeGraph("sadsa")
        return "11"
    }

    @GetMapping
    fun getGraphData(): GraphDataVO {
        return graphService.getGraphData()
    }


    @GetMapping("node/options/{id}")
    fun getActionOptions(@PathVariable id: String): List<NodeOptionVO>{
        return graphService.getNodeOptions(id)
    }

    @GetMapping("templates")
    fun retrieveTemplates(): List<ActionTemplateVO> {
        return graphService.retrieveAllTemplates()
    }

}
