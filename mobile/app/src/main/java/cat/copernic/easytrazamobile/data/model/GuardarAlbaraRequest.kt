package cat.copernic.easytrazamobile.data.model

data class GuardarAlbaraRequest(
    val numeroAlbara: String,
    val proveidor: String,
    val dataRecepcio: String,
    val observacions: String?,
    val usuariId: Long,
    val lots: List<GuardarLotRequest>
)