package tests

import api.RetrofitBuilder
import kotlinx.coroutines.runBlocking
import model.Pet
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import utils.PetUtils
import java.util.concurrent.TimeoutException

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("Тестирование PetStore API")
class PetStoreApiTest {

    private val apiService = RetrofitBuilder.petStoreApiService

    private fun waitForPetCreation(petId: Long, timeoutSeconds: Int = 10): Pet {
        val startTime = System.currentTimeMillis()
        val timeoutMs = timeoutSeconds * 1000L

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                val call = apiService.getPetById(petId)
                val response = call.execute()

                if (response.isSuccessful && response.body() != null) {
                    return response.body()!!
                }

                Thread.sleep(2000)
            } catch (e: Exception) {
                Thread.sleep(1000)
            }
        }

        throw TimeoutException("Pet with id $petId not found within $timeoutSeconds seconds")
    }

    @Test
    @Order(1)
    @DisplayName("POST - Создание нового питомца")
    fun `should create new pet`(){

        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
        val call = apiService.addPet(newPet)
        val response = call.execute()

        assertTrue(response.isSuccessful)
        val createdPet = response.body()!! //TODO: rewrite

        assertEquals(newPet.name, createdPet.name)
        assertEquals(newPet.status, createdPet.status)

        println("Создан питомец с ID: ${createdPet.id}")
    }

    @Test
    @Order(2)
    @DisplayName("GET - Получение питомца по ID")
    fun `should get pet by id`() = runBlocking {
        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
        val createCall = apiService.addPet(newPet)
        val createResponse = createCall.execute()
        val createdPet = createResponse.body()!!

        println("Создан питомец с id= ${createdPet.id}")

        val pet = waitForPetCreation(createdPet.id)

        println("Найден питомец с id= ${pet.id}")
        assertEquals(createdPet.id, pet.id)
        assertEquals(createdPet.name, pet.name)
    }
}