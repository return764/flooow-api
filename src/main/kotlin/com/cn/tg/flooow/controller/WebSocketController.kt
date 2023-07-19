package com.cn.tg.flooow.controller

import com.cn.tg.flooow.entity.vo.MoveNodeEvent
import com.cn.tg.flooow.enums.ReturnType
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.service.GraphExecutionService
import com.cn.tg.flooow.service.GraphService
import com.cn.tg.flooow.service.MessageHandler
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class WebSocketController(
    private val graphService: GraphService,
    private val graphExecutionService: GraphExecutionService,
    private val template: MessageHandler,
) {

    @MessageMapping("/graph/{graphId}/node/create")
    fun createNode(node: Node, @DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphService.addNode(node))
            .returnType(ReturnType.CREATE_NODE)
            .send()
    }

    @MessageMapping("/graph/{graphId}/edge/create")
    fun createEdge(edge: Edge, @DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphService.addEdge(edge))
            .returnType(ReturnType.CREATE_EDGE)
            .send()
    }

    @MessageMapping("/graph/{graphId}/node/move")
    fun moveNode(event: MoveNodeEvent, @DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphService.moveNode(event))
            .returnType(ReturnType.MOVE_NODE)
            .send()
    }

    @MessageMapping("/graph/{graphId}/node/delete")
    fun deleteNode(nodeId: String, @DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphService.deleteNode(nodeId))
            .returnType(ReturnType.DELETE_NODE)
            .send()
    }

    @MessageMapping("/graph/{graphId}/edge/delete")
    fun deleteEdge(edgeId: String, @DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphService.deleteEdge(edgeId))
            .returnType(ReturnType.DELETE_EDGE)
            .send()
    }

    @MessageMapping("/graph/{graphId}/execution")
    fun executeGraph(@DestinationVariable graphId: String) {
        template.builder()
            .destination("/queue/graph/$graphId")
            .payload(graphExecutionService.execute(graphId))
            .returnType(ReturnType.EXECUTION)
            .send()
    }
}
