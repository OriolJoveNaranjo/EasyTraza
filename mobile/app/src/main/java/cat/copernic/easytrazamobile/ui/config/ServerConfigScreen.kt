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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasyScreenHeader
import cat.copernic.easytrazamobile.ui.components.EasySectionCard
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.components.WarmOutlinedTextField
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted

/**
 * Screen that lets the operator configure and test the backend server IP.
 */
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
                title = stringResource(R.string.screen_server_title),
                subtitle = stringResource(R.string.screen_server_subtitle)
            )

            WarmOutlinedTextField(
                value = serverIp,
                onValueChange = viewModel::onIpChange,
                label = stringResource(R.string.server_ip_label),
                placeholder = stringResource(R.string.server_ip_placeholder),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.server_port_hint),
                color = EasyTextMuted
            )

            EasyPrimaryButton(
                text = stringResource(R.string.server_save_button),
                onClick = { viewModel.saveIp(onConfigSaved) }
            )

            EasySecondaryButton(
                text = if (isConnecting) {
                    stringResource(R.string.server_connecting)
                } else {
                    stringResource(R.string.server_test_button)
                },
                onClick = viewModel::testConnection,
                enabled = !isConnecting
            )

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = serverMessageColor(message)
                )
            }
        }
    }
}

/** Chooses the right color for success, neutral and error server messages. */
private fun serverMessageColor(message: String) = when {
    message.contains("correcta", ignoreCase = true) ||
            message.contains("correctament", ignoreCase = true) ||
            message.contains("correctamente", ignoreCase = true) ||
            message.contains("guardat", ignoreCase = true) ||
            message.contains("guardado", ignoreCase = true) -> EasyPrimary

    message.contains("connectant", ignoreCase = true) ||
            message.contains("conectando", ignoreCase = true) -> EasyTextMuted

    else -> EasyDanger
}
