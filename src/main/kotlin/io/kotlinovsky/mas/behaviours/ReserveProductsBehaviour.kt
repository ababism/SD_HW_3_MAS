package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.messages.ReserveProductRequest
import io.kotlinovsky.mas.entities.messages.UpdateMenuRequest
import io.kotlinovsky.mas.extensions.deserialize
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate

/**
 * Действие резервации продуктов.
 * @param getProductsQuantities Функция получения количества продуктов.
 * @param reserveProduct Функция резервации продукта.
 */
class ReserveProductsBehaviour(
    private val getProductsQuantities: () -> Map<Long, Double>,
    private val reserveProduct: (productTypeId: Long, quantity: Double) -> Double
) : CyclicBehaviour() {

    override fun action() {
        val message = agent.receive(MessageTemplate.MatchConversationId(ConversationsIds.RESERVE_PRODUCTS)) ?: return
        val requests = message.deserialize<List<ReserveProductRequest>>()
        val productsQuantities = getProductsQuantities()

        if (!requests.all { (productsQuantities[it.productTypeId] ?: 0.0) >= it.quantity }) {
            return
        }

        val updates = requests.map { (productTypeId, quantity) ->
            UpdateMenuRequest(
                newQuantity = reserveProduct(productTypeId, quantity),
                productTypeId = productTypeId,
            )
        }

        agent.sendMessage(ReceiversTypesIds.MENU_TYPE, ConversationsIds.ACTUALIZE_MENU, updates)
    }
}
