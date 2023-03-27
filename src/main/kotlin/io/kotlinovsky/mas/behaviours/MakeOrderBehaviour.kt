package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.agents.OrderAgent
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.extensions.deserialize
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate

class MakeOrderBehaviour : CyclicBehaviour() {

    override fun action() {
        val message = agent.receive(MessageTemplate.MatchConversationId(ConversationsIds.MAKE_ORDER)) ?: return
        val visitor = message.deserialize<Visitor>()

        for ((index, request) in visitor.dishes.withIndex()) {
            agent
                .containerController
                .createNewAgent(
                    "OrderAgent #${visitor.visitorName} $index",
                    OrderAgent::class.java.name,
                    arrayOf(visitor, request, message.sender)
                ).start()
        }
    }
}
