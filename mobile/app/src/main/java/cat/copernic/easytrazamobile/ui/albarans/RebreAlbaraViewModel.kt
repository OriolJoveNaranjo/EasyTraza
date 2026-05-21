package cat.copernic.easytrazamobile.ui.albarans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
import cat.copernic.easytrazamobile.data.model.GuardarAlbaraRequest
import cat.copernic.easytrazamobile.data.model.GuardarLotRequest
import cat.copernic.easytrazamobile.data.model.LotFormState
import cat.copernic.easytrazamobile.data.model.MateriaPrimeraMobileDto
import cat.copernic.easytrazamobile.data.model.ProveidorMobileDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for receiving supplier delivery notes from mobile.
 *
 * It loads suppliers and raw materials, validates the dynamic lot form and sends the
 * complete delivery note request to the backend.
 */
class RebreAlbaraViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)
    private val userSessionRepository = UserSessionRepository(application)

    private val _proveidors = MutableStateFlow<List<ProveidorMobileDto>>(emptyList())
    val proveidors: StateFlow<List<ProveidorMobileDto>> = _proveidors

    private val _materiesPrimeres = MutableStateFlow<List<MateriaPrimeraMobileDto>>(emptyList())
    val materiesPrimeres: StateFlow<List<MateriaPrimeraMobileDto>> = _materiesPrimeres

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    /** Loads active suppliers used by the mobile delivery form. */
    fun carregarProveidors() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = string(R.string.users_configure_server_first)
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getProveidorsMobile()

                if (response.isSuccessful) {
                    _proveidors.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = string(R.string.receive_load_suppliers_failed)
                }
            } catch (e: Exception) {
                _message.value = string(R.string.server_connection_failed)
            }
        }
    }

    /** Loads raw materials used in each lot line. */
    fun carregarMateriesPrimeres() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = string(R.string.users_configure_server_first)
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getMateriesPrimeresMobile()

                if (response.isSuccessful) {
                    _materiesPrimeres.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = string(R.string.receive_load_materials_failed)
                }
            } catch (e: Exception) {
                _message.value = string(R.string.server_connection_failed)
            }
        }
    }

    /**
     * Validates and saves a supplier delivery note.
     *
     * @param numeroAlbara Delivery note number.
     * @param proveidor Supplier name selected in the UI.
     * @param dataRecepcio Reception date in ISO format.
     * @param observacions Optional observations.
     * @param lots Dynamic lot form state.
     * @param onSuccess Callback invoked after the backend confirms the save.
     */
    fun guardarAlbara(
        numeroAlbara: String,
        proveidor: String,
        dataRecepcio: String,
        observacions: String,
        lots: List<LotFormState>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (_isSaving.value) return@launch
                _isSaving.value = true
                _message.value = ""

                val validationMessage = validateForm(numeroAlbara, proveidor, lots)
                if (validationMessage != null) {
                    _message.value = validationMessage
                    return@launch
                }

                val usuariId = userSessionRepository.userId.first()
                if (usuariId == null) {
                    _message.value = string(R.string.receive_selected_user_missing)
                    return@launch
                }

                val request = GuardarAlbaraRequest(
                    numeroAlbara = numeroAlbara,
                    proveidor = proveidor,
                    dataRecepcio = dataRecepcio,
                    observacions = observacions.ifBlank { null },
                    usuariId = usuariId,
                    lots = lots.map {
                        GuardarLotRequest(
                            materiaPrimera = it.materiaPrimera,
                            quantitat = it.quantitat.replace(",", ".").toDouble(),
                            unitat = it.unitat,
                            identificadorLot = it.identificadorLot,
                            dataCaducitat = it.dataCaducitat.ifBlank { null }
                        )
                    }
                )

                val baseUrl = serverConfigRepository.serverIp.first()
                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.guardarAlbaraMobile(request)

                if (response.isSuccessful) {
                    _message.value = string(R.string.receive_saved_ok)
                    onSuccess()
                } else {
                    val error = response.errorBody()?.string()
                    _message.value = if (error.isNullOrBlank()) {
                        string(R.string.receive_save_failed_code, response.code())
                    } else {
                        error
                    }
                }
            } catch (e: NumberFormatException) {
                _message.value = string(R.string.receive_invalid_quantity_generic)
            } catch (e: Exception) {
                _message.value = string(R.string.receive_save_error, e.message ?: "")
            } finally {
                _isSaving.value = false
            }
        }
    }

    /** Returns a localized validation message, or `null` when the form is valid. */
    private fun validateForm(
        numeroAlbara: String,
        proveidor: String,
        lots: List<LotFormState>
    ): String? {
        if (numeroAlbara.isBlank()) return string(R.string.receive_delivery_number_required)
        if (proveidor.isBlank()) return string(R.string.receive_supplier_required)
        if (lots.isEmpty()) return string(R.string.receive_min_one_lot)

        lots.forEachIndexed { index, lot ->
            val numLot = index + 1
            val quantity = lot.quantitat.replace(",", ".").toDoubleOrNull()

            when {
                lot.materiaPrimera.isBlank() -> return string(R.string.receive_missing_material, numLot)
                lot.quantitat.isBlank() -> return string(R.string.receive_missing_quantity, numLot)
                quantity == null -> return string(R.string.receive_invalid_quantity, numLot)
                quantity <= 0 -> return string(R.string.receive_quantity_positive, numLot)
                lot.unitat.isBlank() -> return string(R.string.receive_missing_unit, numLot)
                lot.identificadorLot.isBlank() -> return string(R.string.receive_missing_identifier, numLot)
                lot.dataCaducitat.isBlank() -> return string(R.string.receive_missing_expiry_date, numLot)
            }
        }

        return null
    }

    private fun string(id: Int, vararg args: Any): String =
        getApplication<Application>().getString(id, *args)
}
