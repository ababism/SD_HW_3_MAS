package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.Logger
import io.kotlinovsky.mas.behaviours.MakeOrderBehaviour
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.removeFromRegistry
import jade.core.Agent

class ManagerAgent : Agent() {

    override fun setup() {
        addToRegistry(name = "Manager agent", type = ReceiversTypesIds.MANAGER_TYPE)
        addBehaviour(MakeOrderBehaviour())
    }

    override fun takeDown() {
        removeFromRegistry()
        Logger.flush()
    }
}
