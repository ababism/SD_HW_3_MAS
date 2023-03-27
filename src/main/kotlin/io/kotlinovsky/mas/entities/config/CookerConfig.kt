package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.Cooker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookerConfig(
    @SerialName("cookers")
    val cookers: List<Cooker>
)
