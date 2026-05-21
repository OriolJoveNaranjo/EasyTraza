package cat.copernic.easytrazamobile.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cat.copernic.easytrazamobile.ui.theme.EasySurface

/**
 * Search field with a small dropdown of matching suggestions.
 *
 * @param value Current search text.
 * @param onValueChange Called when the text changes or a suggestion is selected.
 * @param label Localized field label.
 * @param options All possible suggestions.
 * @param modifier Modifier applied to the component root.
 */
@Composable
fun SearchDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredOptions = options
        .filter { it.isNotBlank() }
        .filter { value.isBlank() || it.contains(value, ignoreCase = true) }
        .distinct()
        .take(8)

    Box(modifier = modifier) {
        WarmOutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = label,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded && filteredOptions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            containerColor = EasySurface
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
