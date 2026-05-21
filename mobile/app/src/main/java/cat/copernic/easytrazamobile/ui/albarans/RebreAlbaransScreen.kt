package cat.copernic.easytrazamobile.ui.albarans

import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.data.model.LotFormState
import cat.copernic.easytrazamobile.ui.components.WarmOutlinedTextField
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate


private val BackgroundWarm = EasyBackground
private val PrimaryWarm = EasyPrimary
private val TextBrown = EasyText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RebreAlbaraScreen(
    ocrText: String = "",
    onBackToMenu: () -> Unit = {},
    onOpenOcrCamera: () -> Unit = {},
    viewModel: RebreAlbaraViewModel = viewModel()
) {
    var numeroAlbara by rememberSaveable { mutableStateOf("") }
    var dataRecepcio by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }
    var proveidor by rememberSaveable { mutableStateOf("") }
    var observacions by rememberSaveable { mutableStateOf("") }

    var lots by remember {
        mutableStateOf(listOf(LotFormState()))
    }

    var expandedProveidors by remember { mutableStateOf(false) }

    val proveidors by viewModel.proveidors.collectAsState()
    val message by viewModel.message.collectAsState()
    val materiesPrimeres by viewModel.materiesPrimeres.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val scrollState = rememberScrollState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            scrollState.animateScrollTo(0)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.carregarProveidors()
        viewModel.carregarMateriesPrimeres()
    }

    LaunchedEffect(ocrText, proveidors) {
        if (ocrText.isNotBlank()) {
            numeroAlbara = detectarNumeroAlbara(ocrText)

            val proveidorDetectat = detectarProveidor(ocrText, proveidors.map { it.nom })
            if (proveidorDetectat.isNotBlank()) {
                proveidor = proveidorDetectat
            }

            val lotsDetectats = detectarLots(ocrText)

            if (lotsDetectats.isNotEmpty()) {
                lots = lotsDetectats.map {
                    LotFormState(
                        identificadorLot = it
                    )
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = EasySurface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("📄") },
                    label = { Text("Rebre") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onBackToMenu,
                    icon = { Text("🏠") },
                    label = { Text("Menú") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWarm)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Rebre albarà",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryWarm
            )

            Text(
                text = "Escaneja o revisa les dades de l'albarà del proveïdor.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextBrown
            )

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = EasyDanger
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EasySurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = onOpenOcrCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Escanejar albarà amb OCR")
                    }

                    WarmOutlinedTextField(
                        value = numeroAlbara,
                        onValueChange = { numeroAlbara = it },
                        label = "Número d'albarà",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            expandedProveidors = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (proveidor.isBlank()) "Seleccionar proveïdor" else proveidor
                        )
                    }

                    DropdownMenu(
                        expanded = expandedProveidors,
                        onDismissRequest = {
                            expandedProveidors = false
                        }
                    ) {
                        proveidors.forEach { proveidorItem ->
                            DropdownMenuItem(
                                text = { Text(proveidorItem.nom) },
                                onClick = {
                                    proveidor = proveidorItem.nom
                                    expandedProveidors = false
                                }
                            )
                        }
                    }

                    WarmOutlinedTextField(
                        value = dataRecepcio,
                        onValueChange = {},
                        readOnly = true,
                        label = "Data recepció",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    WarmOutlinedTextField(
                        value = observacions,
                        onValueChange = { observacions = it },
                        label = "Observacions",
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Text(
                        text = "Lots",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextBrown
                    )

                    lots.forEachIndexed { index, lot ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = EasySurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Lot ${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = TextBrown
                                )

                                var expandedMateria by remember { mutableStateOf(false) }

                                Box {
                                    Button(
                                        onClick = {
                                            expandedMateria = true
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (lot.materiaPrimera.isBlank()) {
                                                "Seleccionar matèria primera"
                                            } else {
                                                lot.materiaPrimera
                                            }
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expandedMateria,
                                        onDismissRequest = {
                                            expandedMateria = false
                                        }
                                    ) {
                                        if (materiesPrimeres.isEmpty()) {
                                            DropdownMenuItem(
                                                text = { Text("No hi ha matèries carregades") },
                                                onClick = {
                                                    expandedMateria = false
                                                }
                                            )
                                        } else {
                                            materiesPrimeres.forEach { materia ->
                                                DropdownMenuItem(
                                                    text = { Text(materia.nom) },
                                                    onClick = {
                                                        lots = lots.toMutableList().also {
                                                            it[index] = it[index].copy(materiaPrimera = materia.nom)
                                                        }
                                                        expandedMateria = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                WarmOutlinedTextField(
                                    value = lot.quantitat,
                                    onValueChange = { value ->
                                        lots = lots.toMutableList().also {
                                            it[index] = it[index].copy(quantitat = value)
                                        }
                                    },
                                    label = "Quantitat",
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                WarmOutlinedTextField(
                                    value = lot.unitat,
                                    onValueChange = { value ->
                                        lots = lots.toMutableList().also {
                                            it[index] = it[index].copy(unitat = value)
                                        }
                                    },
                                    label = "Unitat",
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                WarmOutlinedTextField(
                                    value = lot.identificadorLot,
                                    onValueChange = { value ->
                                        lots = lots.toMutableList().also {
                                            it[index] = it[index].copy(identificadorLot = value)
                                        }
                                    },
                                    label = "Identificador lot",
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                val today = LocalDate.now()

                                Button(
                                    onClick = {
                                        DatePickerDialog(
                                            context,
                                            { _, year, month, dayOfMonth ->
                                                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

                                                lots = lots.toMutableList().also {
                                                    it[index] = it[index].copy(
                                                        dataCaducitat = selectedDate.toString()
                                                    )
                                                }
                                            },
                                            today.year,
                                            today.monthValue - 1,
                                            today.dayOfMonth
                                        ).apply {
                                            datePicker.minDate = System.currentTimeMillis()
                                        }.show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (lot.dataCaducitat.isBlank()) {
                                            "Seleccionar data caducitat"
                                        } else {
                                            "Caducitat: ${lot.dataCaducitat}"
                                        }
                                    )
                                }

                                if (lots.size > 1) {
                                    Button(
                                        onClick = {
                                            lots = lots.toMutableList().also {
                                                it.removeAt(index)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Eliminar lot")
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            lots = lots + LotFormState()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Afegir lot")
                    }

                    Button(
                        onClick = {
                            viewModel.guardarAlbara(
                                numeroAlbara = numeroAlbara,
                                proveidor = proveidor,
                                dataRecepcio = dataRecepcio,
                                observacions = observacions,
                                lots = lots,
                                onSuccess = {
                                    numeroAlbara = ""
                                    dataRecepcio = LocalDate.now().toString()
                                    proveidor = ""
                                    observacions = ""
                                    lots = listOf(LotFormState())
                                    onBackToMenu()
                                }
                            )
                        },
                        enabled = !isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(if (isSaving) "Guardant..." else "Guardar albarà")
                    }
                }
            }
        }
    }
}

private fun detectarNumeroAlbara(text: String): String {
    val regex = Regex(
        pattern = "(?i)(albar[aà]|albaran|n[ºo]?|num|numero)[:\\s-]*([A-Za-z0-9\\-/]+)"
    )

    return regex.find(text)?.groupValues?.getOrNull(2) ?: ""
}

private fun detectarLots(text: String): List<String> {
    val regex = Regex(
        pattern = "(?i)(lot|lote)[\\s:\\-]*([A-Za-z0-9\\-/]+)"
    )

    return regex.findAll(text)
        .mapNotNull { it.groupValues.getOrNull(2) }
        .distinct()
        .toList()
}

private fun detectarProveidor(text: String, proveidors: List<String>): String {
    val textNormalitzat = text.lowercase()

    return proveidors.firstOrNull { proveidor ->
        textNormalitzat.contains(proveidor.lowercase())
    } ?: ""
}