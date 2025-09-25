package api

import model.store.Order
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface StoreApiService {
    @GET("store/inventory")
    fun getInventory(): Call<Map<String, Int>>

    @POST("store/order")
    fun placeOrder(@Body order: Order): Call<Order>

    @GET("store/order/{orderId}")
    fun getOrderById(@Path("orderId") orderId: Long): Call<Order>

    @DELETE("store/order/{orderId}")
    fun deleteOrder(@Path("orderId") orderId: Long): Call<ResponseBody>
}