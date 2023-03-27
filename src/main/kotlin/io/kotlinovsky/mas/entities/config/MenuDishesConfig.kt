package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.MenuDish
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Конфигурация агента меню (список блюд)
 * @property dishes Список блюд, которые заведение может приготовить.
 */
@Serializable
data class MenuDishesConfig(
    @SerialName("menu_dishes")
    val dishes: List<MenuDish>
)
