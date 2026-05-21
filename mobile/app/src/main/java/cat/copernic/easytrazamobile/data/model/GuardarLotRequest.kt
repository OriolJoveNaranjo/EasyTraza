package cat.copernic.easytrazamobile.data.model

/**
 * Request body for one lot inside a supplier delivery reception.
 *
 * @property materiaPrimera Raw material name selected in the mobile app.
 * @property quantitat Numeric quantity to persist.
 * @property unitat Unit for the quantity.
 * @property identificadorLot Supplier lot identifier.
 * @property dataCaducitat Optional expiry date in ISO format (`yyyy-MM-dd`).
 */
data class GuardarLotRequest(
    val materiaPrimera: String,
    val quantitat: Double,
    val unitat: String,
    val identificadorLot: String,
    val dataCaducitat: String?
)
