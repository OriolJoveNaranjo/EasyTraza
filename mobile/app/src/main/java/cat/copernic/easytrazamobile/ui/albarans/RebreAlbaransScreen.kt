package cat.copernic.easytrazamobile.ui.albarans

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.data.model.LotFormState
import cat.copernic.easytrazamobile.ui.components.EasyDangerButton
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasyScreenHeader
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.components.EasySectionCard
import cat.copernic.easytrazamobile.ui.components.EasyStatusMessage
import cat.copernic.easytrazamobile.ui.components.WarmOutlinedTextField
import cat.copernic.easytrazamobile.ui.theme.EasyAccentSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong
import java.time.LocalDate

/**
 * Screen used to receive a supplier delivery note and create its lots from mobile.
 */
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
    var lots by remember { mutableStateOf(listOf(LotFormState())) }
    var expandedProveidors by remember { mutableStateOf(false) }

    val proveidors by viewModel.proveidors.collectAsState()
    val message by viewModel.message.collectAsState()
    val materiesPrimeres by viewModel.materiesPrimeres.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                lots = lotsDetectats.map { LotFormState(identificadorLot = it) }
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
                    label = { Text(stringResource(R.string.common_receive)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EasyPrimary,
                        selectedTextColor = EasyPrimary,
                        indicatorColor = EasyAccentSoft
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onBackToMenu,
                    icon = { Text("🏠") },
                    label = { Text(stringResource(R.string.common_menu)) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = EasyTextMuted,
                        unselectedTextColor = EasyTextMuted
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EasyBackground)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EasyScreenHeader(
                title = stringResource(R.string.receive_title),
                subtitle = stringResource(R.string.receive_subtitle)
            )

            EasyStatusMessage(message = message)

            EasySectionCard {
                EasyPrimaryButton(
                    text = stringResource(R.string.receive_scan_ocr),
                    onClick = onOpenOcrCamera,
                    modifier = Modifier.height(52.dp)
                )

                WarmOutlinedTextField(
                    value = numeroAlbara,
                    onValueChange = { numeroAlbara = it },
                    label = stringResource(R.string.receive_delivery_number),
                    singleLine = true
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    EasySecondaryButton(
                        text = if (proveidor.isBlank()) {
                            stringResource(R.string.receive_select_supplier)
                        } else {
                            proveidor
                        },
                        onClick = { expandedProveidors = true }
                    )
                    DropdownMenu(
                        expanded = expandedProveidors,
                        onDismissRequest = { expandedProveidors = false },
                        containerColor = EasySurface
                    ) {
                        if (proveidors.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.receive_no_suppliers_loaded)) },
                                onClick = { expandedProveidors = false }
                            )
                        } else {
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
                    }
                }

                WarmOutlinedTextField(
                    value = dataRecepcio,
                    onValueChange = {},
                    readOnly = true,
                    label = stringResource(R.string.receive_date),
                    singleLine = true
                )

                WarmOutlinedTextField(
                    value = observacions,
                    onValueChange = { observacions = it },
                    label = stringResource(R.string.receive_observations),
                    minLines = 3
                )
            }

            Text(
                text = stringResource(R.string.receive_lots_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EasyTextStrong
            )

            lots.forEachIndexed { index, lot ->
                var expandedMateria by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EasySurface),
                    border = BorderStroke(1.dp, EasyBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.common_lot_number, index + 1),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EasyTextStrong
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            EasySecondaryButton(
                                text = if (lot.materiaPrimera.isBlank()) {
                                    stringResource(R.string.receive_select_material)
                                } else {
                                    lot.materiaPrimera
                                },
                                onClick = { expandedMateria = true }
                            )
                            DropdownMenu(
                                expanded = expandedMateria,
                                onDismissRequest = { expandedMateria = false },
                                containerColor = EasySurface
                            ) {
                                if (materiesPrimeres.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.receive_no_materials_loaded)) },
                                        onClick = { expandedMateria = false }
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
                            label = stringResource(R.string.common_quantity),
                            singleLine = true
                        )

                        WarmOutlinedTextField(
                            value = lot.unitat,
                            onValueChange = { value ->
                                lots = lots.toMutableList().also {
                                    it[index] = it[index].copy(unitat = value)
                                }
                            },
                            label = stringResource(R.string.common_unit),
                            singleLine = true
                        )

                        WarmOutlinedTextField(
                            value = lot.identificadorLot,
                            onValueChange = { value ->
                                lots = lots.toMutableList().also {
                                    it[index] = it[index].copy(identificadorLot = value)
                                }
                            },
                            label = stringResource(R.string.receive_lot_identifier),
                            singleLine = true
                        )

                        EasySecondaryButton(
                            text = if (lot.dataCaducitat.isBlank()) {
                                stringResource(R.string.receive_select_expiry_date)
                            } else {
                                stringResource(R.string.receive_expiry_date_value, lot.dataCaducitat)
                            },
                            onClick = {
                                val today = LocalDate.now()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                                        lots = lots.toMutableList().also {
                                            it[index] = it[index].copy(dataCaducitat = selectedDate.toString())
                                        }
                                    },
                                    today.year,
                                    today.monthValue - 1,
                                    today.dayOfMonth
                                ).apply {
                                    datePicker.minDate = System.currentTimeMillis()
                                }.show()
                            }
                        )

                        if (lots.size > 1) {
                            EasyDangerButton(
                                text = stringResource(R.string.receive_delete_lot),
                                onClick = {
                                    lots = lots.toMutableList().also { it.removeAt(index) }
                                }
                            )
                        }
                    }
                }
            }

            EasySecondaryButton(
                text = stringResource(R.string.receive_add_lot),
                onClick = { lots = lots + LotFormState() }
            )

            EasyPrimaryButton(
                text = if (isSaving) {
                    stringResource(R.string.receive_saving)
                } else {
                    stringResource(R.string.receive_save)
                },
                enabled = !isSaving,
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
                modifier = Modifier.height(52.dp)
            )
        }
    }
}

/** Detects a delivery note number from OCR text using common labels. */
private fun detectarNumeroAlbara(text: String): String {
    val regex = Regex(
        pattern = "(?i)(albar[aà]|albaran|n[ºo]?|num|numero)[:\\s-]*([A-Za-z0-9\\-/]+)"
    )

    return regex.find(text)?.groupValues?.getOrNull(2) ?: ""
}

/** Detects supplier lot identifiers from OCR text. */
private fun detectarLots(text: String): List<String> {
    val regex = Regex(
        pattern = "(?i)(lot|lote)[\\s:\\-]*([A-Za-z0-9\\-/]+)"
    )

    return regex.findAll(text)
        .mapNotNull { it.groupValues.getOrNull(2) }
        .distinct()
        .toList()
}

/** Detects a supplier name by checking whether the OCR text contains a known supplier. */
private fun detectarProveidor(text: String, proveidors: List<String>): String {
    val textNormalitzat = text.lowercase()

    return proveidors.firstOrNull { proveidor ->
        textNormalitzat.contains(proveidor.lowercase())
    } ?: ""
}
