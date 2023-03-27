package io.kotlinovsky.mas.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Хранимый на складе продукт.
 * @property productId ID продукта.
 * @property productTypeId ID типа продукта.
 * @property productName Название продукта.
 * @property productCompany Название компании производителя.
 * @property productUnit Единица измерения количества продукта.
 * @property productQuantity Количество продукта на складе.
 * @property productCost Цена за единицу продукта.
 * @property productDelivered Дата доставки продукта.
 * @property productValidUntil Срок годности продукта.
 */
@Serializable
data class Product(
    @SerialName("prod_item_id")
    val productId: Long,
    @SerialName("prod_item_type")
    val productTypeId: Long,
    @SerialName("prod_item_name")
    val productName: String,
    @SerialName("prod_item_company")
    val productCompany: String,
    @SerialName("prod_item_unit")
    val productUnit: String,
    @SerialName("prod_item_quantity")
    var productQuantity: Double,
    @SerialName("prod_item_cost")
    val productCost: Double,
    @SerialName("prod_item_delivered")
    val productDelivered: LocalDateTime,
    @SerialName("prod_item_valid_until")
    val productValidUntil: LocalDateTime,
)
