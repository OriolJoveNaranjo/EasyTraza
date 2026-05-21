package cat.copernic.easytrazamobile.data.model

data class GuardarLotRequest(
    val materiaPrimera: String,
    val quantitat: Double,
    val unitat: String,
    val identificadorLot: String,
    val dataCaducitat: String?
)