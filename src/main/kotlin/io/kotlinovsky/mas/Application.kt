package io.kotlinovsky.mas

import io.kotlinovsky.mas.agents.*
import io.kotlinovsky.mas.entities.config.CookerConfig
import io.kotlinovsky.mas.entities.config.EquipmentsConfig
import io.kotlinovsky.mas.entities.config.VisitorsOrdersConfig
import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.AgentContainer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import kotlin.time.DurationUnit

object Application {

    val cookers by lazy {
        File("jsons/cookers.json").inputStream()
            .use { Json.decodeFromStream<CookerConfig>(it) }
            .cookers
            .filter { it.isCookerActive }
    }

    val equipments by lazy {
        File("jsons/equipment.json").inputStream()
            .use { Json.decodeFromStream<EquipmentsConfig>(it) }
            .equipment
            .filter { it.isEquipmentActive }
    }

    fun start() {
        val runtime = Runtime.instance().apply { setCloseVM(true) }
        val profile = ProfileImpl().apply {
            setParameter(Profile.MAIN_HOST, "localhost")
            setParameter(Profile.MAIN_PORT, "8080")
            setParameter(Profile.GUI, "true")
        }

        val container = runtime.createMainContainer(profile).apply {
            createNewAgent("AgentManager", ManagerAgent::class.java.name, null).start()
            createNewAgent("AgentStorage", StockAgent::class.java.name, null).start()
            createNewAgent("AgentMenu", MenuAgent::class.java.name, null).start()
            start()
        }

        createVisitorsAgents(container)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createVisitorsAgents(container: AgentContainer) {
        val visitors = File("jsons/visitors_orders.json").inputStream()
            .use { Json.decodeFromStream<VisitorsOrdersConfig>(it) }
            .visitors
            .sortedBy { it.orderStart }

        visitors.withIndex().forEach { (index, visitor) ->
            container.createNewAgent(
                "AgentVisitor #${visitor.visitorName}",
                VisitorAgent::class.java.name,
                arrayOf(visitor)
            ).start()

            if (index < visitors.size - 1) {
                val diff = visitors[index + 1]
                    .orderStart
                    .toInstant(TimeZone.currentSystemDefault()) - visitor.orderStart.toInstant(TimeZone.currentSystemDefault())
                Thread.sleep(diff.toLong(DurationUnit.MILLISECONDS) / 50)
            }
        }
    }
}
