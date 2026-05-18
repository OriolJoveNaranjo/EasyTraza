package cat.copernic.easytrazamobile.ui.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Configuració del servidor",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Introdueix la IP o URL del backend",
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = serverIp,
            onValueChange = viewModel::onIpChange,
            label = { Text("IP del servidor") },
            placeholder = { Text(
                text = "Exemple: 192.168.1.50:8080",
                style = MaterialTheme.typography.bodySmall
            )},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                viewModel.saveIp()
                onConfigSaved()
            }
        ){
            Text("Guardar configuració")
        }
        Button(
            onClick = viewModel::testConnection,
            enabled = !isConnecting,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(if (isConnecting) "Conectando..." else "Provar connexió")
        }

        if (message.isNotBlank()) {
            Text(
                text = message,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}