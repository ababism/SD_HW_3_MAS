package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.Visitor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisitorsOrdersConfig(
    @SerialName("visitors_orders")
    val visitors: List<Visitor>
)