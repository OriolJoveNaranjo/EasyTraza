package cat.copernic.easytrazamobile.data.model

data class UsuariDto(
    val id: Long,
    val nom: String,
    val email: String? = null,
    val actiu: Boolean = true,
    val foto: String? = null
)