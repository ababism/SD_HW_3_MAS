package io.kotlinovsky.mas.entities.output

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessOutput(
    @SerialName("proc_id")
    val processId: String,
    @SerialName("ord_dish")
    val dishId: Long,
    @SerialName("proc_started")
    val processStarted: LocalDateTime,
    @SerialName("proc_ended")
    val processEnd: LocalDateTime,
    @SerialName("proc_active")
    val isActive: Boolean,
    @SerialName("proc_operations")
    val operations: List<ProcessOperationLog>,
)
