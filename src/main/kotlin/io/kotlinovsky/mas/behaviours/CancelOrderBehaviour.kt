package io.kotlinovsky.mas.behaviours

import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.AID
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate

class CancelOrderBehaviour(
    private val nextSender: AID?,
) : CyclicBehaviour() {

    override fun action() {
        agent.receive(
            MessageTemplate.and(
                MessageTemplate.MatchReplyTo(arrayOf(agent.aid)),
                MessageTemplate.MatchConversationId(ConversationsIds.CANCEL_ORDER)
            )
        ) ?: return

        if (nextSender != null) {
            // https://youtu.be/JHljzy2Gb9A?t=45
            agent.sendMessage(
                receiver = null,
                conversationId = ConversationsIds.CANCEL_ORDER,
                modify = { it.addReplyTo(nextSender) },
                content = "cancelled",
            )
        }

        agent.doDelete()
    }
}