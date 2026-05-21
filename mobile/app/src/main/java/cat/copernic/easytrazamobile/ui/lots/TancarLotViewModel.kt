package cat.copernic.easytrazamobile.ui.lots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.model.LotOberturaDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TancarLotViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)

    private val _lots = MutableStateFlow<List<LotOberturaDto>>(emptyList())
    val lots: StateFlow<List<LotOberturaDto>> = _lots

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun carregarLots() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getLotsOberts()

                if (response.isSuccessful) {
                    _lots.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = "No s'han pogut carregar els lots oberts"
                }

            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            }
        }
    }

    fun tancarLot(lotId: Long) {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()
                val api = RetrofitProvider.createApi(baseUrl)

                val response = api.tancarLotMobile(lotId)

                if (response.isSuccessful) {
                    _message.value = "Lot tancat correctament"
                    carregarLots()
                } else {
                    _message.value = response.errorBody()?.string()
                        ?: "No s'ha pogut tancar el lot"
                }

            } catch (e: Exception) {
                _message.value = "Error tancant el lot: ${e.message}"
            }
        }
    }
}