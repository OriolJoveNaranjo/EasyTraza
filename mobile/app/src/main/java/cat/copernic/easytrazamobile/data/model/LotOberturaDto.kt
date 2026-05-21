package cat.copernic.easytrazamobile.data.model

/**
 * Lot summary used by the open-lot and close-lot screens.
 *
 * @property id Unique backend identifier.
 * @property identificadorLot Supplier lot identifier.
 * @property materiaPrimera Raw material name.
 * @property proveidor Supplier name.
 * @property quantitat Current lot quantity.
 * @property unitat Unit for the quantity.
 * @property estat Current backend lot status.
 */
data class LotOberturaDto(
    val id: Long,
    val identificadorLot: String,
    val materiaPrimera: String,
    val proveidor: String,
    val quantitat: Double,
    val unitat: String,
    val estat: String
)
