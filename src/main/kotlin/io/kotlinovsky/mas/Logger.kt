package io.kotlinovsky.mas

import io.kotlinovsky.mas.entities.output.OperationOutput
import io.kotlinovsky.mas.entities.output.ProcessOutput
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.File

object Logger {

    private var processOutputs = mutableListOf<ProcessOutput>()
    private var operationsOutputs = mutableListOf<OperationOutput>()

    fun writeOperation(operationOutput: OperationOutput) {
        operationsOutputs.add(operationOutput)
    }

    fun writeProcess(processOutput: ProcessOutput) {
        processOutputs.add(processOutput)
    }

    fun flush() {
        val json = Json { prettyPrint = true }

        File("outputs").mkdir()
        File("outputs/process_log.json")
            .apply { delete() }
            .apply { createNewFile() }
            .outputStream()
            .use { json.encodeToStream(processOutputs, it) }
        File("outputs/operation_log.json")
            .apply { delete() }
            .apply { createNewFile() }
            .outputStream()
            .use { json.encodeToStream(operationsOutputs, it) }
    }
}