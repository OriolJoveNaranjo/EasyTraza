package cat.copernic.easytrazamobile.data.model

/**
 * Minimal lot representation embedded in a mobile delivery-note response.
 *
 * @property id Unique backend identifier.
 * @property identificadorLot Supplier lot identifier.
 * @property estat Current backend lot status.
 */
data class LotMobileDto(
    val id: Long,
    val identificadorLot: String,
    val estat: String
)
