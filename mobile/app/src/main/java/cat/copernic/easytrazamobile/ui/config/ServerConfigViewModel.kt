package cat.copernic.easytrazamobile.ui.config

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for reading, validating and testing the backend server configuration.
 */
class ServerConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ServerConfigRepository(application)

    private val _serverIp = MutableStateFlow("")
    val serverIp: StateFlow<String> = _serverIp

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting

    init {
        viewModelScope.launch {
            repository.serverIp.collect { savedIp ->
                _serverIp.value = savedIp
            }
        }
    }

    /** Updates the currently typed IP and clears validation messages. */
    fun onIpChange(newIp: String) {
        _serverIp.value = newIp
        _message.value = ""
    }

    /** Normalizes a plain IP into the backend base URL expected by Retrofit. */
    private fun buildServerUrl(ip: String): String {
        val cleanIp = ip
            .trim()
            .removePrefix("http://")
            .removePrefix("https://")
            .substringBefore(":")

        return "http://$cleanIp:8080"
    }

    /** Validates and saves the backend URL, then invokes [onSaved] when successful. */
    fun saveIp(onSaved: () -> Unit = {}) {
        val ip = _serverIp.value.trim()

        if (ip.isBlank()) {
            _message.value = string(R.string.server_ip_required)
            return
        }

        val regex = Regex("^(\\d{1,3}\\.){3}\\d{1,3}$")
        val ipWithoutProtocol = ip.removePrefix("http://").removePrefix("https://").substringBefore(":")

        if (!regex.matches(ipWithoutProtocol)) {
            _message.value = string(R.string.server_ip_invalid)
            return
        }

        val finalUrl = buildServerUrl(ip)

        viewModelScope.launch {
            repository.saveServerIp(finalUrl)
            _message.value = string(R.string.server_saved, finalUrl)
            onSaved()
        }
    }

    /** Tests whether the configured backend responds to the health endpoint. */
    fun testConnection() {
        val ip = _serverIp.value.trim()

        if (ip.isBlank()) {
            _message.value = string(R.string.server_enter_ip_first)
            return
        }

        val finalUrl = buildServerUrl(ip)

        viewModelScope.launch {
            _isConnecting.value = true
            _message.value = string(R.string.server_connecting)

            try {
                val api = RetrofitProvider.createApi(finalUrl)
                val response = api.testConnection()

                _message.value = if (response.isSuccessful) {
                    string(R.string.server_connection_ok)
                } else {
                    string(R.string.server_invalid_response, response.code())
                }
            } catch (e: Exception) {
                _message.value = string(R.string.server_connection_failed)
            } finally {
                _isConnecting.value = false
            }
        }
    }
/**
 * Compon la interfície de string i connecta els esdeveniments amb la lògica de pantalla.
 */

    private fun string(id: Int, vararg args: Any): String =
        getApplication<Application>().getString(id, *args)
}
