package tests.store

import api.RetrofitBuilder
import model.store.OrderStatus
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import utils.OrderUtils

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Тестирование Store API")
class StoreTest {

    private val apiService = RetrofitBuilder.storeApiService
    private val createdOrders = mutableListOf<Long>()

    @AfterEach
    fun cleanup() {
        createdOrders.forEach { orderId ->
            try {
                apiService.deleteOrder(orderId).execute()
            } catch (e: Exception) {
                println("Cleanup failed for order $orderId: ${e.message}")
            }
        }
        createdOrders.clear()
    }

    @Test
    @DisplayName("GET - Получение инвентаря по статусам")
    fun `should get inventory by status`() {
        val call = apiService.getInventory()
        val response = call.execute()

        assertTrue(response.isSuccessful, "Запрос должен быть успешным")

        val inventory = response.body()!!

        assertTrue(inventory.isNotEmpty(), "Инвентарь не должен быть пустым")

        val expectedStatuses = setOf("available", "pending", "sold")
        assertTrue(inventory.keys.containsAll(expectedStatuses)) {
            "Инвентарь должен содержать статусы: $expectedStatuses"
        }

        inventory.forEach { (status, count) ->
            assertTrue(count >= 0, "Количество для статуса '$status' должно быть неотрицательным")
        }
    }

    @Test
    @DisplayName("POST - Создание нового заказа")
    fun `should place new order`() {
        val newOrder = OrderUtils.generateOrder(
            quantity = 3,
            status = OrderStatus.PLACED,
            complete = false
        )

        val call = apiService.placeOrder(newOrder)
        val response = call.execute()

        assertTrue(response.isSuccessful, "Запрос должен быть успешным")
        assertEquals(200, response.code())

        val createdOrder = response.body()!!
        createdOrders.add(createdOrder.id)

        assertEquals(newOrder, createdOrder)

    }
}