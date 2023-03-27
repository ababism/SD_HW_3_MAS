package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.behaviours.CancelOrderBehaviour
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.removeFromRegistry
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.Agent
import jade.core.behaviours.OneShotBehaviour

class VisitorAgent : Agent() {

    private val visitor by lazy { arguments[0] as Visitor }

    override fun setup() {
        addToRegistry("Visitor agent", ReceiversTypesIds.VISITOR_TYPE)
        addBehaviour(CancelOrderBehaviour(nextSender = null))
        addBehaviour(object : OneShotBehaviour() {
            override fun action() {
                agent.sendMessage(ReceiversTypesIds.MANAGER_TYPE, ConversationsIds.MAKE_ORDER, visitor)
            }
        })
    }

    override fun takeDown() {
        removeFromRegistry()
    }
}