package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.Logger
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.entities.VisitorDish
import io.kotlinovsky.mas.entities.output.ProcessOperationLog
import io.kotlinovsky.mas.entities.output.ProcessOutput
import io.kotlinovsky.mas.extensions.deserialize
import io.kotlinovsky.mas.extensions.hashMd5
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.AID
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate
import kotlinx.datetime.*

class CompleteOperationsBehaviour(
    private val reduceOperationsCount: () -> Int,
    private val sender: AID,
    private val visitor: Visitor,
    private val visitorDish: VisitorDish,
    private val getOperationsIds: () -> List<String>,
) : CyclicBehaviour() {

    private var maxTime: Long = 0

    override fun action() {
        val message = agent.receive(
            MessageTemplate.and(
                MessageTemplate.MatchReplyTo(arrayOf(agent.aid)),
                MessageTemplate.MatchConversationId(ConversationsIds.OPERATION_COMPLETE)
            )
        ) ?: return

        val elapsedTime = message.deserialize<Long>()
        maxTime = elapsedTime.coerceAtLeast(maxTime)

        // Все операции завершены.
        if (reduceOperationsCount() == 0) {
            agent.sendMessage(
                receiver = null,
                conversationId = ConversationsIds.CANCEL_ORDER,
                modify = { it.addReplyTo(sender) },
                content = "complete",
            )

            writeToLog()
            agent.doDelete()
        }
    }

    private fun writeToLog() {
        val processOutput = ProcessOutput(
            processId = agent.aid.name.hashMd5(),
            dishId = visitorDish.dishId,
            processStarted = visitor.orderStart,
            processEnd = visitor.orderStart
                .toInstant(TimeZone.currentSystemDefault())
                .plus(maxTime * 50, DateTimeUnit.MILLISECOND, TimeZone.currentSystemDefault())
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            operations = getOperationsIds().map { ProcessOperationLog(it.hashMd5()) },
            isActive = false,
        )

        Logger.writeProcess(processOutput)
    }
}