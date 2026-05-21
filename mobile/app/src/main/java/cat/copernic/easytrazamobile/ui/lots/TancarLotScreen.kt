package cat.copernic.easytrazamobile.ui.lots

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.ui.components.EasyInfoRow
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasyScreenHeader
import cat.copernic.easytrazamobile.ui.components.EasyStatusMessage
import cat.copernic.easytrazamobile.ui.components.SearchDropdownField
import cat.copernic.easytrazamobile.ui.theme.EasyAccentSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong

@Composable
fun TancarLotScreen(
    onBackToMenu: () -> Unit = {},
    viewModel: TancarLotViewModel = viewModel()
) {
    val lots by viewModel.lots.collectAsState()
    val message by viewModel.message.collectAsState()
    val isClosing by viewModel.isClosing.collectAsState()

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
                    icon = { Text("✅") },
                    label = { Text("Tancar") },
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
                    label = { Text("Menú") },
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
                title = "Tancar lot",
                subtitle = "Busca un lot obert i finalitza'l quan ja no s'estigui utilitzant."
            )

            EasyStatusMessage(message = message)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EasySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, EasyBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Filtres",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EasyTextStrong
                    )

                    SearchDropdownField(
                        value = filtreLot,
                        onValueChange = { filtreLot = it },
                        label = "Número de lot",
                        options = lots.map { it.identificadorLot }
                    )

                    SearchDropdownField(
                        value = filtreProveidor,
                        onValueChange = { filtreProveidor = it },
                        label = "Proveïdor",
                        options = lots.map { it.proveidor }
                    )

                    SearchDropdownField(
                        value = filtreMateria,
                        onValueChange = { filtreMateria = it },
                        label = "Matèria primera",
                        options = lots.map { it.materiaPrimera }
                    )
                }
            }

            if (lotsFiltrats.isEmpty()) {
                Text(
                    text = "No hi ha lots oberts disponibles amb aquests filtres.",
                    color = EasyTextMuted
                )
            }

            lotsFiltrats.forEach { lot ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EasySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EasyBorder),
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

                        EasyInfoRow("Matèria", lot.materiaPrimera)
                        EasyInfoRow("Proveïdor", lot.proveidor)
                        EasyInfoRow("Quantitat", "${lot.quantitat} ${lot.unitat}")
                        EasyInfoRow("Estat", lot.estat)

                        EasyPrimaryButton(
                            text = if (isClosing) "Tancant..." else "Tancar lot",
                            onClick = { viewModel.tancarLot(lot.id) },
                            enabled = !isClosing
                        )
                    }
                }
            }
        }
    }
}
