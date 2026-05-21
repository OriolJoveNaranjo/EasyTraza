package cat.copernic.easytrazamobile.ui.theme

import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyDanger
import cat.copernic.easytrazamobile.ui.theme.EasyAccent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EasyAccent,
    secondary = EasySidebar,
    tertiary = EasyPrimary,
    background = EasyText,
    surface = Color(0xFF2F241C),
    onPrimary = EasySurface,
    onSecondary = EasySurface,
    onTertiary = EasySurface,
    onBackground = EasySurface,
    onSurface = EasySurface
)

private val LightColorScheme = lightColorScheme(
    primary = EasyPrimary,
    secondary = EasySidebar,
    tertiary = EasyAccent,
    background = EasyBackground,
    surface = EasySurface,
    surfaceVariant = EasySurfaceAlt,
    outline = EasyBorder,
    error = EasyDanger,
    onPrimary = EasySurface,
    onSecondary = EasySurface,
    onTertiary = EasySurface,
    onBackground = EasyText,
    onSurface = EasyText,
    onSurfaceVariant = EasyTextMuted
)

@Composable
fun EasyTrazaMobileTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
