package io.kotlinovsky.mas.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Visitor(
    @SerialName("vis_name")
    val visitorName: String,
    @SerialName("vis_ord_started")
    val orderStart: LocalDateTime,
    @SerialName("vis_ord_ended")
    val orderEnd: String?,
    @SerialName("vis_ord_total")
    val orderTotal: Double,
    @SerialName("vis_ord_dishes")
    val dishes: List<VisitorDish>
)
