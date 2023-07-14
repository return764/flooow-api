package com.cn.tg.flooow.service

import com.cn.tg.flooow.entity.ActionOptionPO
import com.cn.tg.flooow.entity.EdgePO
import com.cn.tg.flooow.entity.vo.ActionOptionVO
import com.cn.tg.flooow.entity.vo.ActionTemplateVO
import com.cn.tg.flooow.entity.vo.ActionVO
import com.cn.tg.flooow.repository.ActionRepository
import com.cn.tg.flooow.model.Edge
import com.cn.tg.flooow.entity.vo.GraphDataVO
import com.cn.tg.flooow.entity.vo.MoveNodeEvent
import com.cn.tg.flooow.model.Node
import com.cn.tg.flooow.repository.ActionOptionRepository
import com.cn.tg.flooow.repository.ActionTemplateOptionRepository
import com.cn.tg.flooow.repository.ActionTemplateRepository
import com.cn.tg.flooow.repository.EdgeRepository
import com.cn.tg.flooow.repository.NodeRepository
import com.cn.tg.flooow.repository.PortRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.HashMap

@Service
class GraphService(
    private val actionRepository: ActionRepository,
    private val actionOptionRepository: ActionOptionRepository,
    private val nodeRepository: NodeRepository,
    private val portRepository: PortRepository,
    private val edgeRepository: EdgeRepository,
    private val actionTemplateRepository: ActionTemplateRepository,
    private val actionTemplateOptionRepository: ActionTemplateOptionRepository
) {

    fun getGraphData(): GraphDataVO {
        val listNodes = nodeRepository.findAll()
            .filter { !it.isDeleted }
            .map {
                it.toModel(portRepository.findAllByNodeId(it.id), actionOptionRepository.findAllByNodeId(it.id))
            }
        val listEdges = edgeRepository.findAll()
            .filter { !it.isDeleted }
            .map {
                it.toModel()
            }
        return GraphDataVO(listNodes, listEdges)
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
    fun addNode(node: Node): Node {
        with(node) {
            nodeRepository.save(toPO())
            val template = actionTemplateRepository.findByTemplateName(data.get("template"))
            val action = actionRepository.save(toActionPO(template))
            portRepository.saveAll(buildPortsPOs())
            val templateOptions = actionTemplateOptionRepository.findAllByTemplateId(template?.id)

            templateOptions.map {
                val value = data[it.key]
                ActionOptionPO(
                    actionId = action.id!!,
                    nodeId = node.id,
                    key = it.key,
                    value = value ?: it.defaultValue,
                    type = it.type,
                    visible = it.visible
                )
            }.let { actionOptionRepository.saveAll(it) }
        }

        return node
    }

    fun addEdge(edge: Edge): Edge {
        with(edge) {
            EdgePO(
                id = id,
                shape = shape,
                sourceCellId = source.cell,
                sourcePortId = source.port,
                targetCellId = target.cell,
                targetPortId = target.port
            ).let { edgeRepository.save(it) }
        }
        return edge
    }

    fun retrieveAllTemplates(): List<ActionTemplateVO> {
        return actionTemplateRepository.findAll()
            .map {
                val options = actionTemplateOptionRepository.findAllByTemplateId(it.id)
                ActionTemplateVO(
                    shape = it.shape,
                    data = options.associate { opt ->
                        opt.key to opt.defaultValue
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
           .filter { !it.isDeleted }
           .map { it.toVO() }
    }

    @Transactional
    fun deleteNode(nodeId: String): Boolean {
        nodeRepository.findById(nodeId).ifPresent {
            nodeRepository.save(it.copy(isDeleted = true))
        }

        portRepository.findAllByNodeId(nodeId).forEach {
            portRepository.save(it.copy(isDeleted = true))
        }

        actionRepository.findByNodeId(nodeId).ifPresent {
            actionRepository.save(it.copy(isDeleted = true))
        }

        actionOptionRepository.findAllByNodeId(nodeId).forEach {
            actionOptionRepository.save(it.copy(isDeleted = true))
        }
        return true
    }

    fun deleteEdge(edgeId: String): Boolean {
        edgeRepository.findById(edgeId).ifPresent {
            edgeRepository.save(it.copy(isDeleted = true))
        }
        return true
    }

    fun updateActionOptions(nodeId: String, data: HashMap<String, String>): List<ActionOptionVO> {
        val actionOptions = actionOptionRepository.findAllByNodeId(nodeId)
        val changedActionOptions = actionOptions.map {
            it.copy(
                value = data[it.key] ?: it.value
            )
        }
        return actionOptionRepository.saveAll(changedActionOptions).map { it.toVO() }
    }
}
