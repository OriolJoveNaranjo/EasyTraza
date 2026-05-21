package cat.copernic.easytrazamobile.ui.config

import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.components.WarmOutlinedTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ServerConfigScreen(
    onConfigSaved: () -> Unit,
    viewModel: ServerConfigViewModel = viewModel()
) {
    val serverIp by viewModel.serverIp.collectAsState()
    val message by viewModel.message.collectAsState()
    val isConnecting by viewModel.isConnecting.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EasyBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = EasySurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Configuració del servidor",
                    style = MaterialTheme.typography.headlineMedium,
                    color = EasyPrimary
                )

                Text(
                    text = "Introdueix només la IP del backend. L'app afegeix el port automàticament.",
                    color = EasyTextMuted
                )

                WarmOutlinedTextField(
                    value = serverIp,
                    onValueChange = viewModel::onIpChange,
                    label = "IP del servidor",
                    placeholder = "Exemple: 192.168.1.50",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        viewModel.saveIp()
                        onConfigSaved()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar configuració")
                }

                Button(
                    onClick = viewModel::testConnection,
                    enabled = !isConnecting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isConnecting) "Connectant..." else "Provar connexió")
                }

                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        color = EasyText
                    )
                }
            }
        }
    }
}