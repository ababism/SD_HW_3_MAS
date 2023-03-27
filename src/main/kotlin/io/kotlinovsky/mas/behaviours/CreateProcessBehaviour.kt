package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.agents.ProcessAgent
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.entities.DishCardOperation
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.entities.VisitorDish
import io.kotlinovsky.mas.entities.messages.CreateProcessRequest
import io.kotlinovsky.mas.extensions.deserialize
import io.kotlinovsky.mas.extensions.hashMd5
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate

class CreateProcessBehaviour(
    private val order: VisitorDish,
    private val visitor: Visitor,
    private val saveOperationsCount: (Int) -> Unit,
    private val saveOperationsIds: (List<String>) -> Unit
) : CyclicBehaviour() {

    override fun action() {
        val message = agent.receive(
            MessageTemplate.and(
                MessageTemplate.MatchConversationId(ConversationsIds.CREATE_PROCESS),
                MessageTemplate.MatchReplyTo(arrayOf(agent.aid))
            )
        ) ?: return

        val request = message.deserialize<CreateProcessRequest>()
        val operations = request.operations
        saveOperationsCount(operations.size)

        val aids = operations.map { operation ->
            val controller = agent.containerController.createNewAgent(
                "AgentProcess #${order.dishId} #${operation.operationType} #${visitor.visitorName} #index",
                ProcessAgent::class.java.name,
                arrayOf(agent.aid, operation, visitor, request.dishCardId)
            )

            controller.start()
            controller.name.hashMd5()
        }

        saveOperationsIds(aids)
    }
}
