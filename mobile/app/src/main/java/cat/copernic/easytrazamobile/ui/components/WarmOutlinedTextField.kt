package cat.copernic.easytrazamobile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyFocus
import cat.copernic.easytrazamobile.ui.theme.EasyInputBorder
import cat.copernic.easytrazamobile.ui.theme.EasyInputDisabled
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySurfaceAlt
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted

@Composable
fun WarmOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    minLines: Int = 1
) {
    val placeholderContent: (@Composable () -> Unit)? = if (placeholder.isNullOrBlank()) {
        null
    } else {
        { Text(placeholder) }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholderContent,
        readOnly = readOnly,
        enabled = enabled,
        singleLine = singleLine,
        minLines = minLines,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = EasyText,
            unfocusedTextColor = EasyText,
            disabledTextColor = EasyTextMuted,
            focusedContainerColor = EasySurfaceAlt,
            unfocusedContainerColor = EasySurfaceAlt,
            disabledContainerColor = EasyInputDisabled,
            focusedBorderColor = EasyFocus,
            unfocusedBorderColor = EasyInputBorder,
            disabledBorderColor = EasyBorder,
            focusedLabelColor = EasyPrimary,
            unfocusedLabelColor = EasyTextMuted,
            cursorColor = EasyPrimary
        )
    )
}
