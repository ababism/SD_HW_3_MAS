package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.behaviours.CancelOrderBehaviour
import io.kotlinovsky.mas.behaviours.CompleteOperationsBehaviour
import io.kotlinovsky.mas.behaviours.CreateProcessBehaviour
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.entities.VisitorDish
import io.kotlinovsky.mas.entities.messages.SelectDishFromMenuRequest
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.removeFromRegistry
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.OneShotBehaviour

class OrderAgent : Agent() {

    private var operationsCount = 0
    private val operationsIds = mutableListOf<String>()
    private val visitor by lazy { arguments[0] as Visitor }
    private val order by lazy { arguments[1] as VisitorDish }
    private val sender by lazy { arguments[2] as AID }

    override fun setup() {
        addToRegistry("Order agent", ReceiversTypesIds.ORDER_TYPE)

        addBehaviour(CompleteOperationsBehaviour(::reduceOperationsCount, sender, visitor, order, ::getOperationsIds))
        addBehaviour(CancelOrderBehaviour(nextSender = sender))
        addBehaviour(CreateProcessBehaviour(order, visitor, ::saveOperationsCount, ::saveOperationsIds))
        addBehaviour(object : OneShotBehaviour() {
            override fun action() {
                agent.sendMessage(
                    receiver = ReceiversTypesIds.MENU_TYPE,
                    conversationId = ConversationsIds.SELECT_DISH_FROM_MENU,
                    content = SelectDishFromMenuRequest(order.menuDish)
                )
            }
        })
    }

    private fun saveOperationsIds(ids: List<String>) {
        operationsIds.addAll(ids)
    }

    private fun getOperationsIds(): List<String> {
        return operationsIds
    }

    private fun reduceOperationsCount(): Int {
        return --operationsCount
    }

    private fun saveOperationsCount(operationsCount: Int) {
        this.operationsCount = operationsCount
    }

    override fun takeDown() {
        removeFromRegistry()
    }
}