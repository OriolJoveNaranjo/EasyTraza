package cat.copernic.easytrazamobile.data.model

data class AlbaraMobileDto(
    val id: Long,
    val numeroAlbara: String,
    val dataRecepcio: String?,
    val proveidorId: Long?,
    val proveidorNom: String,
    val lots: List<LotMobileDto>
)