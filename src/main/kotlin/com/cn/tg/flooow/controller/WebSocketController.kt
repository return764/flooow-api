package com.cn.tg.flooow.controller

import com.cn.tg.flooow.entity.vo.MoveNodeEvent
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.service.GraphExecutionService
import com.cn.tg.flooow.service.GraphService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


private const val RETURN_TYPE = "return-type"

@Controller
class WebSocketController(
    private val graphService: GraphService,
    private val graphExecutionService: GraphExecutionService,
    private val template: SimpMessagingTemplate,
) {

    @MessageMapping("/graph/{graphId}/node/create")
    fun createNode(node: Node, @DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphService.addNode(node), mapOf(RETURN_TYPE to "CREATE_NODE"))
    }

    @MessageMapping("/graph/{graphId}/edge/create")
    fun createEdge(edge: Edge, @DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphService.addEdge(edge), mapOf(RETURN_TYPE to "CREATE_EDGE"))
    }

    @MessageMapping("/graph/{graphId}/node/move")
    fun moveNode(event: MoveNodeEvent, @DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphService.moveNode(event), mapOf(RETURN_TYPE to "MOVE_NODE"))
    }

    @MessageMapping("/graph/{graphId}/node/delete")
    fun deleteNode(nodeId: String, @DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphService.deleteNode(nodeId), mapOf(RETURN_TYPE to "DELETE_NODE"))
    }

    @MessageMapping("/graph/{graphId}/edge/delete")
    fun deleteEdge(edgeId: String, @DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphService.deleteEdge(edgeId), mapOf(RETURN_TYPE to "DELETE_EDGE"))
    }

    @MessageMapping("/graph/{graphId}/execution")
    fun executeGraph(@DestinationVariable graphId: String) {
        template.convertAndSend("/queue/graph/$graphId", graphExecutionService.execute(graphId), mapOf(RETURN_TYPE to "EXECUTION"))
    }
}
