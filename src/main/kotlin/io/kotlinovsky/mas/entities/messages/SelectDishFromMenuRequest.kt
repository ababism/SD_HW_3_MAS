package io.kotlinovsky.mas.entities.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelectDishFromMenuRequest(
    @SerialName("dish_id")
    val dishId: Long
)
