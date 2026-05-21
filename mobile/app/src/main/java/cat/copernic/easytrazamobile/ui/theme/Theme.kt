package cat.copernic.easytrazamobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EasyLightColorScheme = lightColorScheme(
    primary = EasyPrimary,
    onPrimary = Color.White,
    primaryContainer = EasyAccentSoft,
    onPrimaryContainer = EasyPrimaryDark,

    secondary = EasySidebar,
    onSecondary = Color.White,
    secondaryContainer = EasyAccentSoft,
    onSecondaryContainer = EasyPrimaryDark,

    tertiary = EasyAccent,
    onTertiary = EasyText,

    background = EasyBackground,
    onBackground = EasyText,
    surface = EasySurface,
    onSurface = EasyText,
    surfaceVariant = EasySurfaceAlt,
    onSurfaceVariant = EasyTextMuted,

    outline = EasyBorder,
    error = EasyDanger,
    onError = Color.White
)

@Composable
fun EasyTrazaMobileTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EasyLightColorScheme,
        typography = Typography,
        content = content
    )
}
