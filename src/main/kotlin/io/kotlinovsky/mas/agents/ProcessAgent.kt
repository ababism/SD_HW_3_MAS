package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.Application
import io.kotlinovsky.mas.Logger
import io.kotlinovsky.mas.consts.ConversationsIds
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.Cooker
import io.kotlinovsky.mas.entities.DishCardOperation
import io.kotlinovsky.mas.entities.Equipment
import io.kotlinovsky.mas.entities.Visitor
import io.kotlinovsky.mas.entities.output.OperationOutput
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.hashMd5
import io.kotlinovsky.mas.extensions.removeFromRegistry
import io.kotlinovsky.mas.extensions.sendMessage
import jade.core.AID
import jade.core.Agent
import jade.core.behaviours.OneShotBehaviour
import kotlinx.datetime.*

class ProcessAgent : Agent() {

    private val creator by lazy { arguments[0] as AID }
    private val operation by lazy { arguments[1] as DishCardOperation }
    private val visitor by lazy { arguments[2] as Visitor }
    private val dishCardId by lazy { arguments[3] as Long }
    private var startTime: Long = 0

    override fun setup() {
        addToRegistry("Process agent", ReceiversTypesIds.PROCESS_TYPE)
        addBehaviour(object : OneShotBehaviour() {
            override fun action() {
                startTime = System.currentTimeMillis()

                var activeCooker: Cooker? = null
                var activeEquipment: Equipment? = null

                while (activeCooker == null || activeEquipment == null) {
                    synchronized(javaClass) {
                        // Получаем свободного повара и нужное оборудование.
                        activeCooker = Application.cookers.firstOrNull { it.isCookerActive }
                        activeEquipment = Application
                            .equipments
                            .firstOrNull { it.isEquipmentActive && it.equipmentType == operation.equipmentType }

                        if (activeCooker != null && activeEquipment != null) {
                            activeCooker!!.isCookerActive = false
                            activeEquipment!!.isEquipmentActive = false
                        }
                    }
                }

                val elapsedTimeToWait = (System.currentTimeMillis() - startTime) / 50
                val waitTimeInMillis = (operation.operationTime * 60L * 1000L / 50).toLong()
                val totalElapsedTime = waitTimeInMillis + elapsedTimeToWait
                Thread.sleep(waitTimeInMillis)

                synchronized(javaClass) {
                    activeCooker!!.isCookerActive = true
                    activeEquipment!!.isEquipmentActive = true
                }

                agent.sendMessage(
                    content = totalElapsedTime,
                    receiver = ReceiversTypesIds.ORDER_TYPE,
                    conversationId = ConversationsIds.OPERATION_COMPLETE,
                    modify = { it.addReplyTo(creator) }
                )

                Logger.writeOperation(OperationOutput(
                    operationId = name.hashMd5(),
                    processId = creator.name.hashMd5(),
                    operationStarted = visitor.orderStart,
                    operationEnd = visitor.orderStart
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(totalElapsedTime * 50, DateTimeUnit.MILLISECOND, TimeZone.currentSystemDefault())
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                    active = false,
                    cardId = dishCardId,
                    equipId = activeEquipment!!.equipmentId,
                    coockerId = activeCooker!!.cookerId
                ))

                doDelete()
            }
        })
    }

    override fun takeDown() {
        removeFromRegistry()
    }
}
