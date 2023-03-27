package io.kotlinovsky.mas.extensions

import jade.lang.acl.ACLMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Десериализует сообщение из контента в формате JSON.
 * @return Десериализованное сообщение указанного типа.
 */
inline fun <reified T> ACLMessage.deserialize(): T {
    return content?.let { Json.decodeFromString<T>(it) }
        ?: throw IllegalArgumentException("Invalid request format!")
}
