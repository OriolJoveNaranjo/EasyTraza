package cat.copernic.easytrazamobile.data.model

/**
 * User returned by the backend for the mobile user-selection screen.
 *
 * The mobile app only uses active users, but the backend can return inactive users too.
 *
 * @property id Unique backend identifier.
 * @property nom Display name shown in the app.
 * @property email Optional email address.
 * @property actiu Whether the user can be selected.
 * @property foto Optional profile image filename stored on the backend.
 */
data class UsuariDto(
    val id: Long,
    val nom: String,
    val email: String? = null,
    val actiu: Boolean = true,
    val foto: String? = null
)
