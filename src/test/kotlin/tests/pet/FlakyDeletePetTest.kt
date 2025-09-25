package tests.pet

import api.RetrofitBuilder
import model.pet.Pet
import model.pet.PetStatus
import org.junit.jupiter.api.*
import utils.PetUtils
import utils.TestConfig

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlakyDeletePetTest {

    private lateinit var pet: Pet
    private val apiService = RetrofitBuilder.petApiService
    private val apiKey = TestConfig.apiKey
    @BeforeAll
    fun `create a pet`() {
        val newPet = PetUtils.generatePet(name = "Барсик", status = PetStatus.available)
        val createCall = apiService.addPet(newPet)
        val createResponse = createCall.execute()

        if (!createResponse.isSuccessful) {
            throw RuntimeException("Failed to create pet: ${createResponse.code()}")
        }

        pet = createResponse.body()!!
    }

    @AfterAll
    fun cleanup() {
        try {
            apiService.deletePet(pet.id).execute()
        } catch (e: Exception) {
            println("Cleanup failed: ${e.message}")
        }
    }

    @DisplayName("DELETE - Удаление питомца по ID")
    @RepeatedTest(2, name = RepeatedTest.LONG_DISPLAY_NAME)
    fun `should get pet by id`() {
        val deleteCall = apiService.deletePet(pet.id, apiKey)
        val deleteResponse = deleteCall.execute()

        Assertions.assertTrue(deleteResponse.isSuccessful, "Запрос на обновление должен быть успешным")
    }

}