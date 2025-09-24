package tests.pet

import api.RetrofitBuilder
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
    @Test
    @Order(1)
    @DisplayName("POST - Создание нового питомца")
    fun `should create new pet`(){

        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
        val call = apiService.addPet(newPet)
        val response = call.execute()

        assertTrue(response.isSuccessful)
        val createdPet = response.body()!! //TODO: rewrite

        assertEquals(newPet, createdPet)
        println("Создан питомец с ID: ${createdPet.id}")
    }

    @Test
    @Order(2)
    @DisplayName("GET - Получение питомцев по статусу")
    fun `should get pets by status`() {
        val findCall = apiService.findPetsByStatus("sold")
        val findResponse = findCall.execute()

        assertTrue(findResponse.isSuccessful, "Запрос должен быть успешным")

        val pets = findResponse.body()!!
        assertTrue(pets.isNotEmpty(), "Список питомцев не должен быть пустым")

        pets.forEach { pet ->
            assertEquals("sold", pet.status, "Все питомцы должны иметь статус 'sold'")
        }

        println("Найдено ${pets.size} питомцев со статусом 'sold'")
    }


    @Test
    @Order(4)
    @DisplayName("PUT - Обновление питомца")
    fun `should update existing pet`(){
        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
        val createCall = apiService.addPet(newPet)
        val response = createCall.execute()
        val createdPet = response.body()!!

        val updatedPet = createdPet.copy(
            name = "Мурзик",
            status = "sold"
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
        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
        val createCall = apiService.addPet(newPet)
        val response = createCall.execute()
        val createdPet = response.body()!!

        val deleteCall = apiService.deletePet(createdPet.id)
        val deleteResponse = deleteCall.execute()

        assertEquals(401, deleteResponse.code())
    }
}