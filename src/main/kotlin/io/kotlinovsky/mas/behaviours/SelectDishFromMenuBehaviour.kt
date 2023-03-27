package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.DishCardOperation
import io.kotlinovsky.mas.entities.messages.CreateProcessRequest
import io.kotlinovsky.mas.entities.messages.ReserveProductRequest
import io.kotlinovsky.mas.entities.messages.SelectDishFromMenuRequest
import io.kotlinovsky.mas.extensions.deserialize
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate
import kotlin.reflect.KFunction0

class SelectDishFromMenuBehaviour(
    private val getProducts: (dishId: Long) -> Map<Long, Double>?,
    private val getOperations: (dishId: Long) -> List<DishCardOperation>,
    private val getDishCardId: (dishId: Long) -> Long,
) : CyclicBehaviour() {

    override fun action() {
        val message =
            agent.receive(MessageTemplate.MatchConversationId(ConversationsIds.SELECT_DISH_FROM_MENU)) ?: return
        val inputRequest = message.deserialize<SelectDishFromMenuRequest>()
        val productsQuantities = getProducts(inputRequest.dishId)

        if (productsQuantities == null) {
            // https://youtu.be/JHljzy2Gb9A?t=45
            agent.sendMessage(
                receiver = ReceiversTypesIds.ORDER_TYPE,
                conversationId = ConversationsIds.CANCEL_ORDER,
                modify = { it.addReplyTo(message.sender) },
                content = "cancelled",
            )

            return
        }

        val requests = productsQuantities.map { (id, quantity) -> ReserveProductRequest(id, quantity) }
        agent.sendMessage(ReceiversTypesIds.STOCK_TYPE, ConversationsIds.RESERVE_PRODUCTS, requests)

        val dishCardId = getDishCardId(inputRequest.dishId)
        val operations = getOperations(inputRequest.dishId)
        val request = CreateProcessRequest(dishCardId, operations)
        agent.sendMessage(ReceiversTypesIds.ORDER_TYPE, ConversationsIds.CREATE_PROCESS, request) {
            it.addReplyTo(message.sender)
        }
    }
}
