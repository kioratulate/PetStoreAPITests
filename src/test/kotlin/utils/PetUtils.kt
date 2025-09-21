package utils

import model.Pet
import model.Tag
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
            name = name,
            photoUrls = listOf("https://example.com/photo1.jpg"),
            tags = listOf(Tag(name = "tag${Random.nextInt(100)}")),
            status = status
        )
    }
}