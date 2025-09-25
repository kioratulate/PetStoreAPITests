package utils

import model.store.Order
import model.store.OrderStatus
import kotlin.math.abs
import kotlin.random.Random
object OrderUtils {
    fun generateOrder(
        id: Long = abs(Random.nextLong()),
        petId: Long = abs(Random.nextLong()),
        quantity: Int = Random.nextInt(1, 10),
        status: OrderStatus = OrderStatus.entries.random(),
        complete: Boolean = Random.nextBoolean()
    ): Order {
        return Order(
            id = id,
            petId = petId,
            quantity = quantity,
            status = status,
            complete = complete
        )
    }
}