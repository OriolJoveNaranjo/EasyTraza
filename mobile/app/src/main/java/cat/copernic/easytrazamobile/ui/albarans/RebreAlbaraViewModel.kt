package cat.copernic.easytrazamobile.ui.albarans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytrazamobile.data.local.ServerConfigRepository
import cat.copernic.easytrazamobile.data.model.MateriaPrimeraMobileDto
import cat.copernic.easytrazamobile.data.model.ProveidorMobileDto
import cat.copernic.easytrazamobile.data.remote.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import cat.copernic.easytrazamobile.data.model.GuardarAlbaraRequest
import cat.copernic.easytrazamobile.data.model.GuardarLotRequest
import cat.copernic.easytrazamobile.data.model.LotFormState
import cat.copernic.easytrazamobile.data.local.UserSessionRepository
class RebreAlbaraViewModel(application: Application) : AndroidViewModel(application) {

    private val serverConfigRepository = ServerConfigRepository(application)

    private val _proveidors = MutableStateFlow<List<ProveidorMobileDto>>(emptyList())
    val proveidors: StateFlow<List<ProveidorMobileDto>> = _proveidors

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message
    private val _materiesPrimeres = MutableStateFlow<List<MateriaPrimeraMobileDto>>(emptyList())
    val materiesPrimeres: StateFlow<List<MateriaPrimeraMobileDto>> = _materiesPrimeres

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving
    private val userSessionRepository = UserSessionRepository(application)



    fun carregarProveidors() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getProveidorsMobile()

                if (response.isSuccessful) {
                    _proveidors.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = "No s'han pogut carregar els proveïdors"
                }

            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            }
        }
    }
    fun carregarMateriesPrimeres() {
        viewModelScope.launch {
            try {
                val baseUrl = serverConfigRepository.serverIp.first()

                if (baseUrl.isBlank()) {
                    _message.value = "Primer configura la IP del servidor"
                    return@launch
                }

                val api = RetrofitProvider.createApi(baseUrl)
                val response = api.getMateriesPrimeresMobile()

                if (response.isSuccessful) {
                    _materiesPrimeres.value = response.body() ?: emptyList()
                    _message.value = ""
                } else {
                    _message.value = "No s'han pogut carregar les matèries primeres"
                }

            } catch (e: Exception) {
                _message.value = "No s'ha pogut connectar amb el servidor"
            }
        }
    }
    fun guardarAlbara(
        numeroAlbara: String,
        proveidor: String,
        dataRecepcio: String,
        observacions: String,
        lots: List<LotFormState>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (_isSaving.value) return@launch
                _isSaving.value = true
                if (numeroAlbara.isBlank()) {
                    _message.value = "El número d'albarà és obligatori"
                    return@launch
                }

                if (proveidor.isBlank()) {
                    _message.value = "Has de seleccionar un proveïdor"
                    return@launch
                }

                if (lots.isEmpty()) {
                    _message.value = "Has d'afegir com a mínim un lot"
                    return@launch
                }

                lots.forEachIndexed { index, lot ->
                    val numLot = index + 1

                    if (lot.materiaPrimera.isBlank()) {
                        _message.value = "Falta seleccionar la matèria primera del lot $numLot"
                        return@launch
                    }

                    if (lot.quantitat.isBlank()) {
                        _message.value = "Falta indicar la quantitat del lot $numLot"
                        return@launch
                    }

                    if (lot.quantitat.replace(",", ".").toDoubleOrNull() == null) {
                        _message.value = "La quantitat del lot $numLot ha de ser un número vàlid"
                        return@launch
                    }

                    if (lot.quantitat.replace(",", ".").toDouble() <= 0) {
                        _message.value = "La quantitat del lot $numLot ha de ser superior a zero"
                        return@launch
                    }

                    if (lot.unitat.isBlank()) {
                        _message.value = "Falta indicar la unitat del lot $numLot"
                        return@launch
                    }

                    if (lot.identificadorLot.isBlank()) {
                        _message.value = "Falta indicar l'identificador del lot $numLot"
                        return@launch
                    }

                    if (lot.dataCaducitat.isBlank()) {
                        _message.value = "Falta seleccionar la data de caducitat del lot $numLot"
                        return@launch
                    }
                }

                val lotsValids = lots

                if (lotsValids.isEmpty()) {
                    _message.value = "Has d'afegir com a mínim un lot complet"
                    return@launch
                }
                val usuariId = userSessionRepository.userId.first()

                if (usuariId == null) {
                    _message.value = "No s'ha trobat l'usuari seleccionat"
                    return@launch
                }
                val request = GuardarAlbaraRequest(
                    numeroAlbara = numeroAlbara,
                    proveidor = proveidor,
                    dataRecepcio = dataRecepcio,
                    observacions = observacions.ifBlank { null },
                    usuariId = usuariId,
                    lots = lotsValids.map {
                        GuardarLotRequest(
                            materiaPrimera = it.materiaPrimera,
                            quantitat = it.quantitat.replace(",", ".").toDouble(),
                            unitat = it.unitat,
                            identificadorLot = it.identificadorLot,
                            dataCaducitat = it.dataCaducitat.ifBlank { null }
                        )
                    }
                )

                val baseUrl = serverConfigRepository.serverIp.first()
                val api = RetrofitProvider.createApi(baseUrl)

                val response = api.guardarAlbaraMobile(request)

                if (response.isSuccessful) {
                    _message.value = "Albarà guardat correctament"
                    onSuccess()
                } else {
                    _message.value = "No s'ha pogut guardar l'albarà: ${response.code()}"
                }

            } catch (e: NumberFormatException) {
                _message.value = "La quantitat ha de ser un número vàlid"
            } catch (e: Exception) {
                _message.value = "Error guardant l'albarà: ${e.message}"
            }
            finally {
                _isSaving.value = false
            }
        }
    }
}