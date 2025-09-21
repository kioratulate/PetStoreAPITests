package api

import model.Pet
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.Response

interface PetStoreApiService {
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
        @Path("petId") petId: Long
    ): Call<ResponseBody>

}