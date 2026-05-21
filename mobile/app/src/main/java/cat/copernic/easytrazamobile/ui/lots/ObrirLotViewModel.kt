package cat.copernic.easytrazamobile.ui.lots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
import cat.copernic.easytrazamobile.data.model.LotOberturaDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ObrirLotViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)
    private val userSessionRepository = UserSessionRepository(application)

    private val _lots = MutableStateFlow<List<LotOberturaDto>>(emptyList())
    val lots: StateFlow<List<LotOberturaDto>> = _lots

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _lotPendentConfirmacio = MutableStateFlow<Long?>(null)
    val lotPendentConfirmacio: StateFlow<Long?> = _lotPendentConfirmacio

    fun carregarLots() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getLotsEnEstoc()

                if (response.isSuccessful) {
                    _lots.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = "No s'han pogut carregar els lots"
                }

            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            }
        }
    }

    fun obrirLot(
        lotId: Long,
        confirmar: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()
                val usuariId = userSessionRepository.userId.first()

                if (usuariId == null) {
                    _message.value = "No s'ha trobat l'usuari seleccionat"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.obrirLotMobile(
                    id = lotId,
                    usuariId = usuariId,
                    confirmar = confirmar
                )

                if (response.isSuccessful) {
                    _message.value = "Lot obert correctament"
                    _lotPendentConfirmacio.value = null
                    carregarLots()

                } else {
                    val error = response.errorBody()?.string()
                        ?: "No s'ha pogut obrir el lot"

                    _message.value = error

                    if (error.contains("Ja hi ha un lot obert", ignoreCase = true)) {
                        _lotPendentConfirmacio.value = lotId
                    }
                }

            } catch (e: Exception) {
                _message.value = "Error obrint el lot: ${e.message}"
            }
        }
    }
}