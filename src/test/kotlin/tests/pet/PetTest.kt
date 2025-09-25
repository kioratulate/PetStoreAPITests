package tests.pet

import api.RetrofitBuilder
import model.pet.PetStatus
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import utils.PetUtils
import utils.TestConfig

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("Тестирование PetStore API")
class PetTest {

    private val apiService = RetrofitBuilder.petApiService
    private val apiKey = TestConfig.apiKey
    private val createdPetIds = mutableListOf<Long>()

    @AfterEach
    fun cleanup() {
        createdPetIds.forEach { petId ->
            try {
                apiService.deletePet(petId, TestConfig.apiKey).execute()
            } catch (e: Exception) {
                println("Cleanup failed for pet $petId: ${e.message}")
            }
        }
        createdPetIds.clear()
    }

    @Test
    @Order(1)
    @DisplayName("POST - Создание нового питомца")
    fun `should create new pet`(){

        val newPet = PetUtils.generatePet(name = "Барсик", status = PetStatus.sold)
        val call = apiService.addPet(newPet)
        val response = call.execute()

        assertTrue(response.isSuccessful)
        val createdPet = response.body()!!
        createdPetIds.add(createdPet.id)
        assertEquals(newPet, createdPet)
    }

    @Test
    @Order(2)
    @DisplayName("GET - Получение питомцев по статусу")
    fun `should get pets by status`() {
        val findCall = apiService.findPetsByStatus(PetStatus.sold)
        val findResponse = findCall.execute()

        assertTrue(findResponse.isSuccessful, "Запрос должен быть успешным")

        val pets = findResponse.body()!!
        assertTrue(pets.isNotEmpty(), "Список питомцев не должен быть пустым")

        pets.forEach { pet ->
            assertEquals(PetStatus.sold, pet.status, "Все питомцы должны иметь статус 'sold'")
        }

    }

    @Test
    @Order(3)
    @DisplayName("GET - Получение питомцев по нескольким статусам")
    fun `should get pets by multiple statuses`() {
        val findCall = apiService.findPetsByStatus(PetStatus.available, PetStatus.sold)
        val findResponse = findCall.execute()

        assertTrue(findResponse.isSuccessful, "Запрос должен быть успешным")

        val pets = findResponse.body()!!
        val expectedStatuses = setOf(PetStatus.available, PetStatus.sold)

        pets.forEach { pet ->
            assertTrue(expectedStatuses.contains(pet.status)) {
                "Питомец имеет неожиданный статус: ${pet.status}"
            }
        }

    }


    @Test
    @Order(4)
    @DisplayName("PUT - Обновление питомца")
    fun `should update existing pet`(){
        val newPet = PetUtils.generatePet(name = "Барсик", status = PetStatus.sold)
        val createCall = apiService.addPet(newPet)
        val response = createCall.execute()
        val createdPet = response.body()!!
        createdPetIds.add(createdPet.id)
        val updatedPet = createdPet.copy(
            name = "Мурзик",
            status = PetStatus.sold
        )

        val updateCall = apiService.updatePet(updatedPet)
        val updateResponse = updateCall.execute()
        val updatePetResult = updateResponse.body()!!

        assertTrue(updateResponse.isSuccessful, "Запрос на обновление должен быть успешным")
        assertEquals(updatedPet, updatePetResult, "Питомцы должны совпадать")
    }

    @Test
    @Order(5)
    @DisplayName("DELETE - Удаление питомца")
    fun `should return error delete non-existing pet`(){

        val deleteCall = apiService.deletePet(-1, apiKey)
        val deleteResponse = deleteCall.execute()

        assertEquals(404, deleteResponse.code())
    }

    @Test
    @Order(6)
    @DisplayName("DELETE - Удаление без авторизации")
    fun `should return error delete without authorization`(){
        val newPet = PetUtils.generatePet(name = "Барсик", status = PetStatus.available)
        val createCall = apiService.addPet(newPet)
        val response = createCall.execute()
        val createdPet = response.body()!!
        createdPetIds.add(createdPet.id)

        val deleteCall = apiService.deletePet(createdPet.id)
        val deleteResponse = deleteCall.execute()

        assertEquals(401, deleteResponse.code())
    }
}