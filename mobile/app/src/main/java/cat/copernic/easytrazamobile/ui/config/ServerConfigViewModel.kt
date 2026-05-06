package cat.copernic.easytrazamobile.ui.config

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider

class ServerConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ServerConfigRepository(application)

    private val _serverIp = MutableStateFlow("")
    val serverIp: StateFlow<String> = _serverIp

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

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

    fun saveIp() {
        val ip = _serverIp.value.trim()

        if (ip.isBlank()) {
            _message.value = "La IP del servidor és obligatòria"
            return
        }

        // Validación simple (IP o URL con puerto)
        val regex = Regex("^((http://)?(\\d{1,3}\\.){3}\\d{1,3}(:\\d{2,5})?)$")

        if (!regex.matches(ip)) {
            _message.value = "Format incorrecte. Exemple: 192.168.1.50:8080"
            return
        }

        // Añadir http:// si no está
        val finalUrl = if (!ip.startsWith("http")) {
            "http://$ip"
        } else {
            ip
        }

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

        val finalUrl = if (!ip.startsWith("http")) {
            "http://$ip"
        } else {
            ip
        }

        viewModelScope.launch {
            try {
                val api = RetrofitProvider.createApi(finalUrl)
                val response = api.testConnection()

                if (response.isSuccessful) {
                    _message.value = "Connexió correcta amb el servidor"
                } else {
                    _message.value = "Servidor trobat, però resposta no vàlida: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            }
        }
    }
}