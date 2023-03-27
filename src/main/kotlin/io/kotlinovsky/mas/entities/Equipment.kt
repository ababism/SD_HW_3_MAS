package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    @SerialName("equip_id")
    val equipmentId: Long,
    @SerialName("equip_type")
    val equipmentType: Long,
    @SerialName("equip_name")
    val equipmentName: String,
    @SerialName("equip_active")
    @Volatile
    var isEquipmentActive: Boolean
)
