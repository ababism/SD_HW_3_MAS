package io.kotlinovsky.mas.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Операция технологической карты.
 * @property operationType Тип операции.
 * @property equipmentType Нужный тип оборудования.
 * @property operationTime Время, требуемое для выполнения операции.
 * @property operationAsyncPoint Флаг для параллелизма.
 * @property products Продукты, требуемые для операции.
 */
@Serializable
data class DishCardOperation(
    @SerialName("oper_type")
    val operationType: Long,
    @SerialName("equip_type")
    val equipmentType: Long,
    @SerialName("oper_time")
    val operationTime: Double,
    @SerialName("oper_async_point")
    val operationAsyncPoint: Long,
    @SerialName("oper_products")
    val products: List<OperationProduct>
)
