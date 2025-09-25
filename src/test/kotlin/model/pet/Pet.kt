package model.pet
import kotlinx.serialization.Serializable
@Serializable
data class Pet(
    val id: Long = 0,
    val category: Category? = null,
    val name: String? = null,
    val photoUrls: List<String>,
    val tags: List<Tag>? = null,
    val status: PetStatus
)

@Serializable
enum class PetStatus {
    available,
    pending,
    sold
}