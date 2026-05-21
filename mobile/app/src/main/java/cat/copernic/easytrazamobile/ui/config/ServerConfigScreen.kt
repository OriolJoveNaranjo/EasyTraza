package cat.copernic.easytrazamobile.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasyScreenHeader
import cat.copernic.easytrazamobile.ui.components.EasySectionCard
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.components.EasyStatusMessage
import cat.copernic.easytrazamobile.ui.components.WarmOutlinedTextField
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted

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
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        EasySectionCard {
            EasyScreenHeader(
                title = "Servidor",
                subtitle = "Configura la IP del backend abans de carregar dades."
            )

            WarmOutlinedTextField(
                value = serverIp,
                onValueChange = viewModel::onIpChange,
                label = "IP del servidor",
                placeholder = "Exemple: 192.168.1.50",
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "El port 8080 s'afegeix automàticament.",
                color = EasyTextMuted
            )

            EasyPrimaryButton(
                text = "Guardar configuració",
                onClick = {
                    viewModel.saveIp(onConfigSaved)
                }
            )

            EasySecondaryButton(
                text = if (isConnecting) "Connectant..." else "Provar connexió",
                onClick = viewModel::testConnection,
                enabled = !isConnecting
            )

            if (message.isNotBlank()) {
                val messageColor = when {
                    message.contains("correcta", ignoreCase = true) ||
                            message.contains("correctament", ignoreCase = true) ||
                            message.contains("guardat", ignoreCase = true) ||
                            message.contains("guardado", ignoreCase = true) -> EasyPrimary

                    message.contains("connectant", ignoreCase = true) ||
                            message.contains("conectando", ignoreCase = true) -> EasyTextMuted

                    else -> EasyDanger
                }

                Text(
                    text = message,
                    color = messageColor
                )
            }
        }
    }
}
