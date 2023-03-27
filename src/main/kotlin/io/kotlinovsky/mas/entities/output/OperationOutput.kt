package io.kotlinovsky.mas.entities.output

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OperationOutput(
    @SerialName("oper_id")
    val operationId: String,
    @SerialName("proc_id")
    val processId: String,
    @SerialName("oper_card")
    val cardId: Long,
    @SerialName("oper_started")
    val operationStarted: LocalDateTime,
    @SerialName("oper_ended")
    val operationEnd: LocalDateTime,
    @SerialName("oper_equip_id")
    val equipId: Long,
    @SerialName("oper_coocker_id")
    val coockerId: Long,
    @SerialName("oper_active")
    val active: Boolean
)