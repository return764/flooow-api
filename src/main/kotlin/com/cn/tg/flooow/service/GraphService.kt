package com.cn.tg.flooow.service

import com.cn.tg.flooow.controller.request.GraphCreationRequest
import com.cn.tg.flooow.entity.ActionOptionPO
import com.cn.tg.flooow.entity.GraphPO
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.entity.vo.ActionVO
import com.cn.tg.flooow.repository.ActionRepository
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.entity.vo.GraphSummaryVO
import com.cn.tg.flooow.enums.OptionInputType
import com.cn.tg.flooow.entity.vo.MoveNodeEvent
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.repository.ActionOptionRepository
import com.cn.tg.flooow.repository.ActionTemplateOptionRepository
import com.cn.tg.flooow.repository.ActionTemplateRepository
import com.cn.tg.flooow.repository.EdgeRepository
import com.cn.tg.flooow.repository.GraphRepository
import com.cn.tg.flooow.repository.NodeRepository
import com.cn.tg.flooow.repository.PortRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class GraphService(
    private val actionRepository: ActionRepository,
    private val actionOptionRepository: ActionOptionRepository,
    private val graphRepository: GraphRepository,
    private val nodeRepository: NodeRepository,
    private val portRepository: PortRepository,
    private val edgeRepository: EdgeRepository,
    private val actionTemplateRepository: ActionTemplateRepository,
    private val actionTemplateOptionRepository: ActionTemplateOptionRepository
) {
    fun getGraphData(graphId: String): GraphDataVO {
        val listNodes = nodeRepository.findAllByGraphId(graphId)
            .map {
                it.toModel(portRepository.findAllByNodeId(it.id), actionOptionRepository.findAllByNodeId(it.id))
            }
        val listEdges = edgeRepository.findAllByGraphId(graphId)
            .map {
                it.toModel()
            }
        return GraphDataVO(listNodes, listEdges)
    }

    fun retrieveAllGraph(): List<GraphSummaryVO> {
        return graphRepository.findAll().map { it.toSummary()}
    }

    fun getAction(nodeId: String): ActionVO {
        return actionRepository.findByNodeId(nodeId).map {
            val template = actionTemplateRepository.findById(it.templateId!!).get()
            ActionVO(
                id = it.id!!,
                templateName = template.templateName,
                className = template.className,
                status = it.status,
                value = it.value
            )
        }.get()
    }

    @Transactional
    fun addNode(graphId: String, node: Node): Node {
        return with(node) {
            val template = actionTemplateRepository.findByTemplateName(data["template"] as String)
            val action = actionRepository.save(toActionPO(template))
            val ports = portRepository.saveAll(buildPortsPOs())
            val templateOptions = actionTemplateOptionRepository.findAllByTemplateId(template?.id)

            val options = templateOptions.map {
                val value = data[it.key]
                ActionOptionPO(
                    actionId = action.id!!,
                    nodeId = node.id,
                    key = it.key,
                    type = it.type,
                    typeValue = it.defaultTypeValue.copy(value = value ?: it.defaultTypeValue.value),
                    inputType = OptionInputType.DEFAULT,
                    visible = it.visible
                )
            }.let { actionOptionRepository.saveAll(it) }
            nodeRepository.save(toPO(data["label"] as String, graphId)).toModel(ports, options)
        }
    }

    fun addEdge(graphId: String, edge: Edge): Edge {
        edge.toPO(graphId).let { edgeRepository.save(it) }
        return edge
    }

    fun retrieveAllTemplates(): List<ActionTemplateVO> {
        return actionTemplateRepository.findAll()
            .map {
                val options = actionTemplateOptionRepository.findAllByTemplateId(it.id)
                ActionTemplateVO(
                    shape = it.shape,
                    data = options.associate { opt ->
                        opt.key to opt.defaultTypeValue.value
                    }
                )
            }
    }

    fun moveNode(event: MoveNodeEvent): MoveNodeEvent {
        nodeRepository.findById(event.id)
            .ifPresent { nodeRepository.save(it.copy(
                x = event.postX,
                y = event.postY
            )) }
        return event
    }

    fun getActionOptions(nodeId: String): List<ActionOptionVO> {
       return actionOptionRepository.findAllByNodeId(nodeId)
           .filter { it.visible }
           .map { it.toVO() }
    }

    @Transactional
    fun deleteNode(nodeId: String): Boolean {
        nodeRepository.findById(nodeId).ifPresent {
            nodeRepository.delete(it)
        }

        portRepository.findAllByNodeId(nodeId).forEach {
            portRepository.delete(it)
        }

        actionRepository.findByNodeId(nodeId).ifPresent {
            actionRepository.delete(it)
        }

        actionOptionRepository.findAllByNodeId(nodeId).forEach {
            actionOptionRepository.delete(it)
        }
        return true
    }

    fun deleteEdge(edgeId: String): Boolean {
        edgeRepository.findById(edgeId).ifPresent {
            edgeRepository.delete(it)
        }
        return true
    }

    fun updateActionOptions(nodeId: String, listActionOptions: List<ActionOptionVO>): List<ActionOptionVO> {
        val actionOptions = actionOptionRepository.findAllByNodeId(nodeId)
        val mappedActionOptions = listActionOptions.associateBy { it.id }
        val changedActionOptions = actionOptions.map {
            it.copy(
                inputType = mappedActionOptions[it.id]?.inputType ?: it.inputType,
                typeValue = it.typeValue.copy(
                    value = mappedActionOptions[it.id]?.value ?: it.typeValue.value
                )
            )
        }
        return actionOptionRepository.saveAll(changedActionOptions).map { it.toVO() }
    }

    fun createGraph(request: GraphCreationRequest): GraphSummaryVO {
        val graph = GraphPO(name = request.name)
        return graphRepository.save(graph).toSummary()
    }

    fun deleteGraph(id: String): GraphSummaryVO {
        val graph = graphRepository.findById(id).orElseThrow()
        return graphRepository.delete(graph).let { graph.toSummary() }
    }
}
