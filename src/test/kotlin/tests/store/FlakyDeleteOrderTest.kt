package tests.store

import api.RetrofitBuilder
import model.store.Order
import org.junit.jupiter.api.*
import utils.OrderUtils

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlakyDeleteOrderTest {

    private lateinit var order: Order
    private val apiService = RetrofitBuilder.storeApiService

    @BeforeAll
    fun `create an order`() {
        val newOrder = OrderUtils.generateOrder()
        val createCall = apiService.placeOrder(newOrder)
        val createResponse = createCall.execute()

        if (!createResponse.isSuccessful) {
            throw RuntimeException("Failed to create order: ${createResponse.code()}")
        }

        order = createResponse.body()!!
    }

    @AfterAll
    fun cleanup() {
        try {
            apiService.deleteOrder(order.id).execute()
        } catch (e: Exception) {
            println("Cleanup failed: ${e.message}")
        }
    }

    @DisplayName("DELETE - Удаление заказа по ID")
    @RepeatedTest(2, name = RepeatedTest.LONG_DISPLAY_NAME)
    fun `should delete order by id`() {
        val deleteCall = apiService.deleteOrder(order.id)
        val deleteResponse = deleteCall.execute()

        Assertions.assertTrue(deleteResponse.isSuccessful, "Запрос на удаление должен быть успешным")
    }
}