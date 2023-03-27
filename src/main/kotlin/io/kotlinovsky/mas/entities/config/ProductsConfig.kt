package io.kotlinovsky.mas.entities.config

import io.kotlinovsky.mas.entities.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Конфигурация агента хранилища (склада).
 * @property products Изначально размещенные на складе продукты.
 */
@Serializable
data class ProductsConfig(
    @SerialName("products")
    val products: List<Product>
)
