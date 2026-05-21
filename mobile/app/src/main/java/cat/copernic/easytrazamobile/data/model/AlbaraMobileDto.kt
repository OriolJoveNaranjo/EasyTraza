package cat.copernic.easytrazamobile.data.model

/**
 * Supplier delivery note response returned by the mobile API.
 *
 * @property id Unique backend identifier.
 * @property numeroAlbara Delivery note number.
 * @property dataRecepcio Reception date in ISO format.
 * @property proveidorId Optional supplier identifier.
 * @property proveidorNom Supplier display name.
 * @property lots Lots associated with the delivery note.
 */
data class AlbaraMobileDto(
    val id: Long,
    val numeroAlbara: String,
    val dataRecepcio: String?,
    val proveidorId: Long?,
    val proveidorNom: String,
    val lots: List<LotMobileDto>
)
