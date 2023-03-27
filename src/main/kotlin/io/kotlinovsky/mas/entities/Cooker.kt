package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cooker(
    @SerialName("cook_id")
    val cookerId: Long,
    @SerialName("cook_name")
    val cookerName: String,
    @SerialName("cook_active")
    @Volatile
    var isCookerActive: Boolean,
)
