package cat.copernic.easytrazamobile.data.model

/**
 * Mutable form state for one supplier lot in the delivery reception screen.
 *
 * The values are kept as strings because they are edited directly in text fields and are
 * validated before being converted to request DTOs.
 *
 * @property materiaPrimera Selected raw material name.
 * @property quantitat Quantity written by the operator.
 * @property unitat Unit written by the operator.
 * @property identificadorLot Supplier lot identifier.
 * @property dataCaducitat Expiry date in ISO format (`yyyy-MM-dd`).
 */
data class LotFormState(
    val materiaPrimera: String = "",
    val quantitat: String = "",
    val unitat: String = "",
    val identificadorLot: String = "",
    val dataCaducitat: String = ""
)
