package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.Equipment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EquipmentsConfig(
    @SerialName("equipment")
    val equipment: List<Equipment>
)