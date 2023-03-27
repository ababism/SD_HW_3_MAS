package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisitorDish(
    @SerialName("ord_dish_id")
    val dishId: Long,
    @SerialName("menu_dish")
    val menuDish: Long,
)
