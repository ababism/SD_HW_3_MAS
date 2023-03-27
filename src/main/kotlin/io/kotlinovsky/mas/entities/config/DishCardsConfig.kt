package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.DishCard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Конфиг технологических карт.
 * @property dishCards Список технологических карт.
 */
@Serializable
data class DishCardsConfig(
    @SerialName("dish_cards")
    val dishCards: List<DishCard>
)
