package io.kotlinovsky.mas.agents

import io.kotlinovsky.mas.behaviours.ReserveProductsBehaviour
import io.kotlinovsky.mas.consts.ReceiversTypesIds
import io.kotlinovsky.mas.entities.config.ProductsConfig
import io.kotlinovsky.mas.extensions.addToRegistry
import io.kotlinovsky.mas.extensions.removeFromRegistry
import jade.core.Agent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

/**
 * Агент склада товаров.
 * Проверяет наличие товара и резервирует его.
 */
class StockAgent : Agent() {

    @OptIn(ExperimentalSerializationApi::class)
    private val products by lazy {
        File("jsons/products.json").inputStream()
            .use { Json.decodeFromStream<ProductsConfig>(it) }
            .products
    }

    override fun setup() {
        addToRegistry(name = "Stock agent", type = ReceiversTypesIds.STOCK_TYPE)
        addBehaviour(
            ReserveProductsBehaviour(
                getProductsQuantities = ::getProductsQuantities,
                reserveProduct = ::reserveProduct
            )
        )
    }

    private fun getProductsQuantities(): Map<Long, Double> = mutableMapOf<Long, Double>().apply {
        products.forEach { this[it.productTypeId] = (this[it.productTypeId] ?: 0.0) + it.productQuantity }
    }

    private fun reserveProduct(productTypeId: Long, quantity: Double): Double {
        var currentQuantity = quantity

        products.asSequence()
            .takeWhile { currentQuantity > 0 }
            .filter { it.productTypeId == productTypeId }
            .forEach { product ->
                val usedQuantity = product
                    .productQuantity
                    .coerceAtMost(currentQuantity)

                product.productQuantity -= usedQuantity
                currentQuantity -= usedQuantity
            }

        return products.asSequence()
            .filter { it.productTypeId == productTypeId }
            .sumOf { it.productQuantity }
    }

    override fun takeDown() {
        removeFromRegistry()
    }
}
