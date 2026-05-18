package cat.copernic.easytrazamobile.ui.config

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    fun onIpChange(newIp: String) {
        _serverIp.value = newIp
        _message.value = ""
    }

    private fun buildServerUrl(ip: String): String {
        val cleanIp = ip
            .trim()
            .removePrefix("http://")
            .removePrefix("https://")
            .substringBefore(":")

        return "http://$cleanIp:8080"
    }

    fun saveIp() {
        val ip = _serverIp.value.trim()

        if (ip.isBlank()) {
            _message.value = "La IP del servidor és obligatòria"
            return
        }

        val regex = Regex("^(\\d{1,3}\\.){3}\\d{1,3}$")

        if (!regex.matches(ip.removePrefix("http://").removePrefix("https://").substringBefore(":"))) {
            _message.value = "Format incorrecte. Exemple: 192.168.1.50"
            return
        }

        val finalUrl = buildServerUrl(ip)

        viewModelScope.launch {
            repository.saveServerIp(finalUrl)
            _message.value = "Servidor guardat: $finalUrl"
        }
    }

    fun testConnection() {
        val ip = _serverIp.value.trim()

        if (ip.isBlank()) {
            _message.value = "Primer has d'introduir la IP del servidor"
            return
        }

        val finalUrl = buildServerUrl(ip)

        viewModelScope.launch {
            _isConnecting.value = true
            _message.value = "Conectando..."

            try {
                val api = RetrofitProvider.createApi(finalUrl)
                val response = api.testConnection()

                _message.value = if (response.isSuccessful) {
                    "Connexió correcta amb el servidor"
                } else {
                    "Servidor trobat, però resposta no vàlida: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            } finally {
                _isConnecting.value = false
            }
        }
    }
}