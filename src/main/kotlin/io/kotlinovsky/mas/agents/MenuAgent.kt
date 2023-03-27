package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.behaviours.ActualizeMenuBehaviour
import io.kotlinovsky.mas.behaviours.SelectDishFromMenuBehaviour
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.DishCardOperation
import io.kotlinovsky.mas.entities.config.DishCardsConfig
import io.kotlinovsky.mas.entities.config.MenuDishesConfig
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.removeFromRegistry
import jade.core.Agent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

/**
 * Агент меню.
 * Осуществляет актуализацию меню.
 */
class MenuAgent : Agent() {

    @OptIn(ExperimentalSerializationApi::class)
    private val dishes by lazy {
        File("jsons/menu_dishes.json").inputStream()
            .use { Json.decodeFromStream<MenuDishesConfig>(it) }
            .dishes
            .associateBy { it.menuDishId }
            .toMutableMap()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val dishCards by lazy {
        File("jsons/dish_cards.json").inputStream()
            .use { Json.decodeFromStream<DishCardsConfig>(it) }
            .dishCards
            .associateBy { it.cardId }
    }

    override fun setup() {
        addToRegistry(name = "Menu agent", type = ReceiversTypesIds.MENU_TYPE)
        addBehaviour(ActualizeMenuBehaviour(updateQuantity = ::updateQuantity))
        addBehaviour(SelectDishFromMenuBehaviour(getProducts = ::getProducts, getOperations = ::getOperations, getDishCardId = ::getDishCardId))
    }

    private fun getDishCardId(dishId: Long): Long {
        return dishes[dishId]!!.menuDishCardId
    }

    private fun getProducts(dishId: Long): Map<Long, Double>? {
        val dish = dishes[dishId]

        // Блюда нет, либо оно неактивно (т.е. не может быть приготовлено).
        if (dish == null || !dish.isMenuDishActive) {
            return null
        }

        val dishCard = dishCards[dish.menuDishCardId] ?: return null
        val needQuantities = mutableMapOf<Long, Double>()
        dishCard.operations
            .asSequence()
            .flatMap { it.products }
            .forEach { needQuantities[it.productType] = (needQuantities[it.productType] ?: 0.0) + it.productQuantity }

        return needQuantities
    }

    private fun updateQuantity(productTypeId: Long, newQuantity: Double) {
        dishes.forEach { (_, dish) ->
            dishCards[dish.menuDishCardId]?.let { card ->
                val needQuantity = card.operations
                    .asSequence()
                    .flatMap { it.products }
                    .filter { it.productType == productTypeId }
                    .map { it.productQuantity }
                    .sum()

                dish.isMenuDishActive = needQuantity <= newQuantity
            }
        }
    }

    private fun getOperations(dishId: Long): List<DishCardOperation> {
        return dishCards[dishes[dishId]!!.menuDishCardId]!!.operations
    }

    override fun takeDown() {
        removeFromRegistry()
    }
}
