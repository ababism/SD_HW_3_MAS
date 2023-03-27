package io.kotlinovsky.mas.entities.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessOperationLog(
    @SerialName("proc_oper")
    val operationId: String
)
