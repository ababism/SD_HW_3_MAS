package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Технологическая карта приготовления блюда.
 * @property cardId ID технологической карты.
 * @property dishName Название блюда.
 * @property cardDescription Описание карты.
 * @property cardTime Время, требуемое для выполнения карты.
 * @property operations Операции, предусмотренные картой.
 */
@Serializable
data class DishCard(
    @SerialName("card_id")
    val cardId: Long,
    @SerialName("dish_name")
    val dishName: String,
    @SerialName("card_descr")
    val cardDescription: String,
    @SerialName("card_time")
    val cardTime: Double,
    @SerialName("operations")
    val operations: List<DishCardOperation>,
)
