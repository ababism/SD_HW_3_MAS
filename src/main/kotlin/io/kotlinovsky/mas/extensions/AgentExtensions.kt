package io.kotlinovsky.mas.extensions

import io.kotlinovsky.mas.consts.ReceiversTypesIds
import jade.core.AID
import jade.core.Agent
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.domain.FIPAAgentManagement.ServiceDescription
import jade.lang.acl.ACLMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Регистрирует агент.
 * @param name Название агента.
 * @param type Тип агента.
 */
fun Agent.addToRegistry(name: String, type: String) {
    val agentDescription = DFAgentDescription().apply { setName(aid) }
    val serviceDescription = ServiceDescription().apply {
        this.name = name
        this.type = type
    }

    agentDescription.addServices(serviceDescription)
    DFService.register(this, agentDescription)
}

/**
 * Отправляет сообщение в указанный канал.
 * @param receiver Тип приемника сообщения.
 * @param conversationId ID канала, в который будет произведена отправка.
 * @param content Сообщение, которое мы отправляем в канал.
 * @param modify Callback для модификации сообщения.
 */
inline fun <reified T> Agent.sendMessage(
    receiver: String?,
    conversationId: String,
    content: T,
    noinline modify: ((ACLMessage) -> Unit)? = null
) {
    val serviceDescription = ServiceDescription().apply { type = receiver }
    val agentDescription = DFAgentDescription().apply { addServices(serviceDescription) }
    val message = ACLMessage(ACLMessage.CFP).apply {
        this.content = Json.encodeToString(content)
        this.conversationId = conversationId
    }

    DFService
        .search(this, agentDescription)
        .forEach { desc -> message.addReceiver(desc.name) }

    modify?.invoke(message)
    send(message)
}

/**
 * Удаляет агент из реестра.
 */
fun Agent.removeFromRegistry() {
    DFService.deregister(this)
}
