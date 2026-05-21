package cat.copernic.easytrazamobile.data.model

/**
 * Supplier option used by the mobile delivery form.
 *
 * @property id Unique backend identifier.
 * @property nom Supplier display name.
 */
data class ProveidorMobileDto(
    val id: Long,
    val nom: String
)
