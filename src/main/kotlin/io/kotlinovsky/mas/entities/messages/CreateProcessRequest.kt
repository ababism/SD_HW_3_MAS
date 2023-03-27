package io.kotlinovsky.mas.entities.messages

import io.kotlinovsky.mas.entities.DishCardOperation
import kotlinx.serialization.Serializable

@Serializable
data class CreateProcessRequest(
    val dishCardId: Long,
    val operations: List<DishCardOperation>,
)