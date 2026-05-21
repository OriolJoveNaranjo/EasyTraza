package cat.copernic.easytrazamobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cat.copernic.easytrazamobile.ui.theme.EasyAccentSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import cat.copernic.easytrazamobile.ui.theme.EasyDangerBg
import cat.copernic.easytrazamobile.ui.theme.EasyDangerBorder
import cat.copernic.easytrazamobile.ui.theme.EasyInfo
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyPrimaryDark
import cat.copernic.easytrazamobile.ui.theme.EasySuccess
import cat.copernic.easytrazamobile.ui.theme.EasySuccessBg
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong

@Composable
fun EasyScreenHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = EasyTextStrong
        )
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = EasyTextMuted
            )
        }
    }
}

@Composable
fun EasySectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = EasySurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, EasyBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            content()
        }
    }
}

@Composable
fun EasyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EasyPrimary,
            contentColor = Color.White,
            disabledContainerColor = EasyAccentSoft,
            disabledContentColor = EasyTextMuted
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EasySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, EasyBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = EasyPrimaryDark,
            disabledContentColor = EasyTextMuted
        )
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun EasyDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = EasyDanger,
            contentColor = Color.White,
            disabledContainerColor = EasyDangerBorder,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EasyStatusMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    if (message.isBlank()) return

    val success = message.contains("correctament", ignoreCase = true) ||
            message.contains("guardat", ignoreCase = true) ||
            message.contains("guardada", ignoreCase = true)

    val background = if (success) EasySuccessBg else EasyDangerBg
    val textColor = if (success) EasySuccess else EasyDanger
    val borderColor = if (success) EasySuccess.copy(alpha = 0.25f) else EasyDangerBorder

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = message,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EasyInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            color = EasyTextMuted,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = EasyText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
