package cat.copernic.easytrazamobile.data.model

/**
 * Request body used by the mobile app to create a supplier delivery note.
 *
 * @property numeroAlbara Delivery note number.
 * @property proveidor Supplier name.
 * @property dataRecepcio Reception date in ISO format (`yyyy-MM-dd`).
 * @property observacions Optional free-text observations.
 * @property usuariId Identifier of the mobile user who created the delivery note.
 * @property lots Lots included in the delivery note.
 */
data class GuardarAlbaraRequest(
    val numeroAlbara: String,
    val proveidor: String,
    val dataRecepcio: String,
    val observacions: String?,
    val usuariId: Long,
    val lots: List<GuardarLotRequest>
)
