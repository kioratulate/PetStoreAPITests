package utils

import model.pet.Category
import model.pet.Pet
import model.pet.PetStatus
import kotlin.math.abs
import kotlin.random.Random

object PetUtils {
    fun generatePet(
        id: Long = abs(Random.nextLong()),
        name: String = "Pet${Random.nextInt()}",
        status: PetStatus = PetStatus.entries.random()
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
}