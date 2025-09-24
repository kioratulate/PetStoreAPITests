package api

import model.pet.Pet
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PetApiService {
    @GET("pet/findByStatus")
    fun findPetsByStatus(
        @Query("status") status: String
    ): Call<List<Pet>>

    @GET("pet/{petId}")
    fun getPetById(
        @Path("petId") petId: Long
    ): Call<Pet>

    @POST("pet")
    fun addPet(
        @Body pet: Pet
    ): Call<Pet>

    @PUT("pet")
    fun updatePet(
        @Body pet: Pet
    ): Call<Pet>

    @DELETE("pet/{petId}")
    fun deletePet(
        @Path("petId") petId: Long,
        @Header("api_key") apiKey: String? = null // Добавляем параметр для заголовка
    ): Call<ResponseBody>

}