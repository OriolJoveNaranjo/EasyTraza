package cat.copernic.easytrazamobile.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
import cat.copernic.easytrazamobile.data.model.UsuariDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class UserSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)
    private val userSessionRepository = UserSessionRepository(application)

    private val _usuaris = MutableStateFlow<List<UsuariDto>>(emptyList())
    val usuaris: StateFlow<List<UsuariDto>> = _usuaris

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _baseUrl = MutableStateFlow("")
    val baseUrl: StateFlow<String> = _baseUrl
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun loadUsers() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val savedBaseUrl = serverConfigRepository.serverIp.first()

                if (savedBaseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                _baseUrl.value = savedBaseUrl

                val api = RetrofitProvider.createApi(savedBaseUrl)
                val response = api.getUsuaris()

                if (response.isSuccessful) {
                    _usuaris.value = response.body()
                        ?.filter { it.actiu }
                        ?: emptyList()

                    _message.value = ""
                } else {
                    _message.value = "No s'han pogut carregar els usuaris: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = when {
                    e.message?.contains("failed to connect", ignoreCase = true) == true ||
                            e.message?.contains("timeout", ignoreCase = true) == true ||
                            e.message?.contains("ECONNREFUSED", ignoreCase = true) == true -> {
                        "No s'ha pogut connectar amb el servidor. Comprova que el backend estigui arrancat."
                    }

                    else -> {
                        "Error carregant usuaris."
                    }
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun selectUser(userId: Long, onSelected: () -> Unit) {
        viewModelScope.launch {
            userSessionRepository.saveUserId(userId)
            onSelected()
        }
    }
    fun startAutoRefreshUsers() {
        viewModelScope.launch {
            while (true) {
                loadUsers()
                delay(5000)
            }
        }
    }
}