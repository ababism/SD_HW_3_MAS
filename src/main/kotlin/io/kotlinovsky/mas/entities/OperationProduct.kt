package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Продукт, требуемый для операции.
 * @property productType Тип продукта.
 * @property productQuantity Количество продукта.
 */
@Serializable
data class OperationProduct(
    @SerialName("prod_type")
    val productType: Long,
    @SerialName("prod_quantity")
    val productQuantity: Double
)
