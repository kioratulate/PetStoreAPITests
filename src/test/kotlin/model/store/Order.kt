package model.store

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Long = 0,
    val petId: Long = 0,
    val quantity: Int = 0,
    val shipDate: String? = null,
    val status: OrderStatus = OrderStatus.PLACED,
    val complete: Boolean = false
)

@Serializable
enum class OrderStatus {
    PLACED,
    APPROVED,
    DELIVERED
}