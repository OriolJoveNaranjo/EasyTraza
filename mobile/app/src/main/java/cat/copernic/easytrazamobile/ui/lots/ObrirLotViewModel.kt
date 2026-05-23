package cat.copernic.easytrazamobile.ui.lots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
import cat.copernic.easytrazamobile.data.model.LotOberturaDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for opening supplier lots from the mobile app.
 */
class ObrirLotViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)
    private val userSessionRepository = UserSessionRepository(application)

    private val _lots = MutableStateFlow<List<LotOberturaDto>>(emptyList())
    val lots: StateFlow<List<LotOberturaDto>> = _lots

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _lotPendentConfirmacio = MutableStateFlow<Long?>(null)
    val lotPendentConfirmacio: StateFlow<Long?> = _lotPendentConfirmacio

    private val _isOpening = MutableStateFlow(false)
    val isOpening: StateFlow<Boolean> = _isOpening

    /** Loads lots currently in stock and available to open. */
    fun carregarLots() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = string(R.string.users_configure_server_first)
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getLotsEnEstoc()

                if (response.isSuccessful) {
                    _lots.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = string(R.string.open_lots_load_failed)
                }
            } catch (e: Exception) {
                _message.value = string(R.string.server_connection_failed)
            }
        }
    }

    /**
     * Opens the selected lot.
     *
     * When the backend reports that another lot of the same material is already open,
     * the lot id is stored so the UI can show the confirmation button.
     */
    fun obrirLot(
        lotId: Long,
        confirmar: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                if (_isOpening.value) return@launch
                _isOpening.value = true

                val baseUrl = serverConfigRepository.serverIp.first()
                val usuariId = userSessionRepository.userId.first()

                if (usuariId == null) {
                    _message.value = string(R.string.receive_selected_user_missing)
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.obrirLotMobile(
                    id = lotId,
                    usuariId = usuariId,
                    confirmar = confirmar
                )

                if (response.isSuccessful) {
                    _message.value = string(R.string.open_lot_success)
                    _lotPendentConfirmacio.value = null
                    carregarLots()
                } else {
                    val error = response.errorBody()?.string() ?: string(R.string.open_lot_failed)
                    _message.value = error

                    if (error.contains("Ja hi ha un lot obert", ignoreCase = true) ||
                        error.contains("Ya hay un lote abierto", ignoreCase = true)
                    ) {
                        _lotPendentConfirmacio.value = lotId
                    }
                }
            } catch (e: Exception) {
                _message.value = string(R.string.open_lot_error, e.message ?: "")
            } finally {
                _isOpening.value = false
            }
        }
    }
/**
 * Compon la interfície de string i connecta els esdeveniments amb la lògica de pantalla.
 */

    private fun string(id: Int, vararg args: Any): String =
        getApplication<Application>().getString(id, *args)
}
