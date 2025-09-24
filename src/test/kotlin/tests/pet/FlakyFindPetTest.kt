package tests.pet

import api.RetrofitBuilder
import model.pet.Pet
import org.junit.jupiter.api.*
import utils.PetUtils
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.jvm.Throws

@TestInstance(Lifecycle.PER_CLASS)
class FlakyFindPetTest {

    private lateinit var pet: Pet
    private val apiService = RetrofitBuilder.petApiService

    @BeforeAll
    fun `create a pet`() {
        val newPet = PetUtils.generatePet(name = "Барсик", status = "available")
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

    @DisplayName("GET - Получение питомца по ID")
    @RepeatedTest(6, name = RepeatedTest.LONG_DISPLAY_NAME)
    fun `should get pet by id`() {
        val findByIdCall = apiService.getPetById(pet.id)
        val findByIdResponse = findByIdCall.execute()

        Assertions.assertTrue(findByIdResponse.isSuccessful) {
            "Request failed with code: ${findByIdResponse.code()}"
        }

        val foundPet = findByIdResponse.body()!!
        Assertions.assertEquals(pet, foundPet)
    }

}