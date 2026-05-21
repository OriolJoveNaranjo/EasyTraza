package cat.copernic.easytrazamobile.ui.lots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.model.LotOberturaDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for closing supplier lots from the mobile app.
 */
class TancarLotViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)

    private val _lots = MutableStateFlow<List<LotOberturaDto>>(emptyList())
    val lots: StateFlow<List<LotOberturaDto>> = _lots

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isClosing = MutableStateFlow(false)
    val isClosing: StateFlow<Boolean> = _isClosing

    /** Loads currently open lots. */
    fun carregarLots() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = string(R.string.users_configure_server_first)
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getLotsOberts()

                if (response.isSuccessful) {
                    _lots.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = string(R.string.close_lots_load_failed)
                }
            } catch (e: Exception) {
                _message.value = string(R.string.server_connection_failed)
            }
        }
    }

    /** Closes the selected lot and reloads the open-lot list. */
    fun tancarLot(lotId: Long) {
        viewModelScope.launch {
            try {
                if (_isClosing.value) return@launch
                _isClosing.value = true

                val baseUrl = serverConfigRepository.serverIp.first()
                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.tancarLotMobile(lotId)

                if (response.isSuccessful) {
                    _message.value = string(R.string.close_lot_success)
                    carregarLots()
                } else {
                    _message.value = response.errorBody()?.string() ?: string(R.string.close_lot_failed)
                }
            } catch (e: Exception) {
                _message.value = string(R.string.close_lot_error, e.message ?: "")
            } finally {
                _isClosing.value = false
            }
        }
    }

    private fun string(id: Int, vararg args: Any): String =
        getApplication<Application>().getString(id, *args)
}
