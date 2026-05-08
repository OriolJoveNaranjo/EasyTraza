package cat.copernic.easytrazamobile.data.model

data class UsuariDto(
    val id: Long,
    val nom: String,
    val cognoms: String? = null,
    val email: String? = null,
    val actiu: Boolean = true
)