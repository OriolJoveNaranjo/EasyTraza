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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.ui.components.SearchDropdownField

private val BackgroundWarm = Color(0xFFFFF7ED)
private val PrimaryWarm = Color(0xFF2F6B4F)
private val TextBrown = Color(0xFF3B240D)

@Composable
fun TancarLotScreen(
    onBackToMenu: () -> Unit = {},
    viewModel: TancarLotViewModel = viewModel()
) {
    val lots by viewModel.lots.collectAsState()
    val message by viewModel.message.collectAsState()

    var filtreLot by rememberSaveable { mutableStateOf("") }
    var filtreProveidor by rememberSaveable { mutableStateOf("") }
    var filtreMateria by rememberSaveable { mutableStateOf("") }
    val isClosing by viewModel.isClosing.collectAsState()

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
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("✅") },
                    label = { Text("Tancar") }
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tancar lot",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryWarm
            )

            Text(
                text = "Selecciona un lot obert per finalitzar-lo.",
                color = TextBrown
            )

            SearchDropdownField(
                value = filtreLot,
                onValueChange = { filtreLot = it },
                label = "Filtrar per número de lot",
                options = lots.map { it.identificadorLot },
                modifier = Modifier.fillMaxWidth()
            )

            SearchDropdownField(
                value = filtreProveidor,
                onValueChange = { filtreProveidor = it },
                label = "Filtrar per proveïdor",
                options = lots.map { it.proveidor },
                modifier = Modifier.fillMaxWidth()
            )

            SearchDropdownField(
                value = filtreMateria,
                onValueChange = { filtreMateria = it },
                label = "Filtrar per matèria primera",
                options = lots.map { it.materiaPrimera },
                modifier = Modifier.fillMaxWidth()
            )

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = if (message.contains("correctament")) PrimaryWarm else Color.Red
                )
            }

            if (lotsFiltrats.isEmpty()) {
                Text(
                    text = "No hi ha lots oberts disponibles.",
                    color = TextBrown
                )
            }

            lotsFiltrats.forEach { lot ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = lot.identificadorLot,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextBrown
                        )

                        Text("Matèria: ${lot.materiaPrimera}", color = TextBrown)
                        Text("Proveïdor: ${lot.proveidor}", color = TextBrown)
                        Text("Quantitat: ${lot.quantitat} ${lot.unitat}", color = TextBrown)
                        Text("Estat: ${lot.estat}", color = TextBrown)

                        Button(
                            onClick = {
                                viewModel.tancarLot(lot.id)
                            },
                            enabled = !isClosing,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isClosing) "Tancant..." else "Tancar lot")
                        }
                    }
                }
            }
        }
    }
}