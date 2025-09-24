package utils

import model.pet.Category
import model.pet.Pet
import kotlin.math.abs
import kotlin.random.Random

object PetUtils {
    fun generatePet(
        id: Long = abs(Random.nextLong()),
        name: String = "Pet${Random.nextInt()}",
        status: String = listOf("available", "pending", "sold").random()
    ): Pet {
        return Pet(
            id = id,
            category = Category(id=0, name="string"),
            name = name,
            photoUrls = listOf("https://example.com/photo1.jpg"),
            tags = listOf(),
            status = status
        )
    }
    fun comparePetContent(pet1: Pet, pet2: Pet): Boolean {
        return pet1.copy(id=0) == pet2.copy(id=0)
    }
}