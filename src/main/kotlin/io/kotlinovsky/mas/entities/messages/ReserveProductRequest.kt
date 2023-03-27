package io.kotlinovsky.mas.entities.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на резервацию продукта на складе.
 * @property productTypeId ID типа продукта.
 * @property quantity Количество резервируемого продукта.
 */
@Serializable
data class ReserveProductRequest(
    @SerialName("productId")
    val productTypeId: Long,
    @SerialName("quantity")
    val quantity: Double
)
