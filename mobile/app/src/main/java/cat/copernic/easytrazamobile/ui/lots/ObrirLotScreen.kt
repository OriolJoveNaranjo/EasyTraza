package cat.copernic.easytrazamobile.ui.lots

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.ui.components.EasyInfoRow
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasyScreenHeader
import cat.copernic.easytrazamobile.ui.components.EasySectionCard
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.components.EasyStatusMessage
import cat.copernic.easytrazamobile.ui.components.SearchDropdownField
import cat.copernic.easytrazamobile.ui.theme.EasyAccentSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong

/**
 * Screen that lists stock lots and allows the operator to open one of them.
 */
@Composable
fun ObrirLotScreen(
    onBackToMenu: () -> Unit = {},
    viewModel: ObrirLotViewModel = viewModel()
) {
    val lots by viewModel.lots.collectAsState()
    val message by viewModel.message.collectAsState()
    val lotPendentConfirmacio by viewModel.lotPendentConfirmacio.collectAsState()
    val isOpening by viewModel.isOpening.collectAsState()

    var filtreLot by rememberSaveable { mutableStateOf("") }
    var filtreProveidor by rememberSaveable { mutableStateOf("") }
    var filtreMateria by rememberSaveable { mutableStateOf("") }

    val lotsFiltrats = lots.filter { lot ->
        lot.identificadorLot.contains(filtreLot, ignoreCase = true) &&
                lot.proveidor.contains(filtreProveidor, ignoreCase = true) &&
                lot.materiaPrimera.contains(filtreMateria, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        viewModel.carregarLots()
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = EasySurface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("📦") },
                    label = { Text(stringResource(R.string.common_open)) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EasyScreenHeader(
                title = stringResource(R.string.open_lot_title),
                subtitle = stringResource(R.string.open_lot_subtitle)
            )

            EasyStatusMessage(message = message)

            EasySectionCard {
                Text(
                    text = stringResource(R.string.common_filters),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EasyTextStrong
                )
                SearchDropdownField(
                    value = filtreLot,
                    onValueChange = { filtreLot = it },
                    label = stringResource(R.string.filter_lot_number),
                    options = lots.map { it.identificadorLot }
                )
                SearchDropdownField(
                    value = filtreProveidor,
                    onValueChange = { filtreProveidor = it },
                    label = stringResource(R.string.filter_supplier),
                    options = lots.map { it.proveidor }
                )
                SearchDropdownField(
                    value = filtreMateria,
                    onValueChange = { filtreMateria = it },
                    label = stringResource(R.string.filter_material),
                    options = lots.map { it.materiaPrimera }
                )
            }

            if (lotsFiltrats.isEmpty()) {
                Text(
                    text = stringResource(R.string.open_lot_empty),
                    color = EasyTextMuted
                )
            }

            lotsFiltrats.forEach { lot ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EasySurface),
                    border = BorderStroke(1.dp, EasyBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = lot.identificadorLot,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EasyTextStrong
                        )

                        EasyInfoRow(stringResource(R.string.common_material), lot.materiaPrimera)
                        EasyInfoRow(stringResource(R.string.common_supplier), lot.proveidor)
                        EasyInfoRow(stringResource(R.string.common_quantity), "${lot.quantitat} ${lot.unitat}")
                        EasyInfoRow(stringResource(R.string.common_status), lot.estat)

                        EasyPrimaryButton(
                            text = if (isOpening) {
                                stringResource(R.string.open_lot_opening)
                            } else {
                                stringResource(R.string.open_lot_button)
                            },
                            onClick = { viewModel.obrirLot(lot.id) },
                            enabled = !isOpening
                        )

                        if (lotPendentConfirmacio == lot.id) {
                            Text(
                                text = stringResource(R.string.open_lot_already_open),
                                color = EasyDanger,
                                fontWeight = FontWeight.Bold
                            )
                            EasySecondaryButton(
                                text = stringResource(R.string.open_lot_confirm_close_previous),
                                onClick = {
                                    viewModel.obrirLot(
                                        lotId = lot.id,
                                        confirmar = true
                                    )
                                },
                                enabled = !isOpening
                            )
                        }
                    }
                }
            }
        }
    }
}
