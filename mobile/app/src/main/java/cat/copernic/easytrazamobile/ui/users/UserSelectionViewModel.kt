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

class UserSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)
    private val userSessionRepository = UserSessionRepository(application)

    private val _usuaris = MutableStateFlow<List<UsuariDto>>(emptyList())
    val usuaris: StateFlow<List<UsuariDto>> = _usuaris

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getUsuaris()

                if (response.isSuccessful) {
                    _usuaris.value = response.body() ?: emptyList()
                } else {
                    _message.value = "No s'han pogut carregar els usuaris"
                }
            } catch (e: Exception) {
                _message.value = "Error carregant usuaris"
            }
        }
    }

    fun selectUser(userId: Long, onSelected: () -> Unit) {
        viewModelScope.launch {
            userSessionRepository.saveUserId(userId)
            onSelected()
        }
    }
}