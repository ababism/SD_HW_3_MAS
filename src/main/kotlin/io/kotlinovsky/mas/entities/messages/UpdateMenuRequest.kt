package io.kotlinovsky.mas.entities.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на актуализацию меню в зависимости от нового количества продукта.
 * @property productTypeId ID типа продукта.
 * @property newQuantity Новое количество продукта.
 */
@Serializable
data class UpdateMenuRequest(
    @SerialName("product_id")
    val productTypeId: Long,
    @SerialName("new_quantity")
    val newQuantity: Double
)
