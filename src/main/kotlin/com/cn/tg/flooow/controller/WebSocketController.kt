package com.cn.tg.flooow.controller

import com.cn.tg.flooow.entity.vo.MoveNodeEvent
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.service.GraphService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class WebSocketController(
    private val graphService: GraphService,
    private val template: SimpMessagingTemplate,
) {

    @MessageMapping("/graph/{id}/node/create")
    fun createNode(node: Node, @DestinationVariable id: String) {
        template.convertAndSend("/queue/graph/$id", graphService.addNode(node))
    }

    @MessageMapping("/graph/{id}/edge/create")
    fun createEdge(edge: Edge, @DestinationVariable id: String) {
        template.convertAndSend("/queue/graph/$id", graphService.addEdge(edge))
    }

    @MessageMapping("/graph/{id}/node/move")
    fun moveNode(event: MoveNodeEvent, @DestinationVariable id: String) {
        template.convertAndSend("/queue/graph/$id", graphService.moveNode(event))
    }
}
