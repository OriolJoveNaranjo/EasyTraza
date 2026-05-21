package cat.copernic.easytrazamobile.data.model

data class LotOberturaDto(
    val id: Long,
    val identificadorLot: String,
    val materiaPrimera: String,
    val proveidor: String,
    val quantitat: Double,
    val unitat: String,
    val estat: String
)