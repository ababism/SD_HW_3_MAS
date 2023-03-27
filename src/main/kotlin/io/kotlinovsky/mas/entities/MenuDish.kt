package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Блюдо из меню.
 * @property menuDishId ID блюда в меню.
 * @property menuDishCardId ID технологической карты блюда.
 * @property menuDishPrice Стоимость приготоавления блюда.
 * @property isMenuDishActive Возможно ли сейчас заказать это блюдо?
 */
@Serializable
data class MenuDish(
    @SerialName("menu_dish_id")
    val menuDishId: Long,
    @SerialName("menu_dish_card")
    val menuDishCardId: Long,
    @SerialName("menu_dish_price")
    val menuDishPrice: Double,
    @SerialName("menu_dish_active")
    var isMenuDishActive: Boolean
)
