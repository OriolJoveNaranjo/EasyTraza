package cat.copernic.easytrazamobile.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
import cat.copernic.easytrazamobile.data.model.UsuariDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the user-selection screen.
 *
 * It loads active users from the backend, refreshes them periodically and stores the
 * selected user identifier in the local mobile session.
 */
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

    private var autoRefreshJob: Job? = null

    /** Loads active users from the configured backend. */
    fun loadUsers() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val savedBaseUrl = serverConfigRepository.serverIp.first()

                if (savedBaseUrl.isBlank()) {
                    _message.value = string(R.string.users_configure_server_first)
                    return@launch
                }

                _baseUrl.value = savedBaseUrl

                val api = RetrofitProvider.createApi(savedBaseUrl)
                val response = api.getUsuaris()

                if (response.isSuccessful) {
                    _usuaris.value = response.body()?.filter { it.actiu } ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = string(R.string.users_load_failed_code, response.code())
                }
            } catch (e: Exception) {
                _message.value = when {
                    e.message?.contains("failed to connect", ignoreCase = true) == true ||
                            e.message?.contains("timeout", ignoreCase = true) == true ||
                            e.message?.contains("ECONNREFUSED", ignoreCase = true) == true -> {
                        string(R.string.users_backend_not_running)
                    }

                    else -> string(R.string.users_load_error)
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /** Stores the selected user and moves to the next screen. */
    fun selectUser(userId: Long, onSelected: () -> Unit) {
        viewModelScope.launch {
            userSessionRepository.saveUserId(userId)
            onSelected()
        }
    }

    /** Starts a single periodic refresh job for user changes made in the backend. */
    fun startAutoRefreshUsers() {
        if (autoRefreshJob?.isActive == true) return

        autoRefreshJob = viewModelScope.launch {
            while (true) {
                loadUsers()
                delay(5000)
            }
        }
    }

    private fun string(id: Int, vararg args: Any): String =
        getApplication<Application>().getString(id, *args)
}
