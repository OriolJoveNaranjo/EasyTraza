package cat.copernic.easytrazamobile.data.model

/**
 * Raw material option used when creating or filtering lots on mobile.
 *
 * @property id Unique backend identifier.
 * @property nom Raw material display name.
 */
data class MateriaPrimeraMobileDto(
    val id: Long,
    val nom: String
)
