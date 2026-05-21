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

@Composable
fun SearchDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var expandedManuallyClosed by remember { mutableStateOf(false) }

    val filteredOptions = options
        .filter { it.contains(value, ignoreCase = true) }
        .distinct()
        .take(8)

    val expanded = value.isNotBlank() && filteredOptions.isNotEmpty() && !expandedManuallyClosed

    Box(modifier = modifier) {
        WarmOutlinedTextField(
            value = value,
            onValueChange = {
                expandedManuallyClosed = false
                onValueChange(it)
            },
            label = label,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expandedManuallyClosed = true }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expandedManuallyClosed = true
                    }
                )
            }
        }
    }
}
