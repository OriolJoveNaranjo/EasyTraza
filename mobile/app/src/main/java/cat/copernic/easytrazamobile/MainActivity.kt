package cat.copernic.easytrazamobile

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import cat.copernic.easytrazamobile.ui.albarans.OcrCameraScreen
import cat.copernic.easytrazamobile.ui.albarans.RebreAlbaraScreen
import cat.copernic.easytrazamobile.ui.theme.EasyTrazaMobileTheme
import cat.copernic.easytrazamobile.ui.config.ServerConfigScreen
import cat.copernic.easytrazamobile.ui.menu.MenuScreen
import cat.copernic.easytrazamobile.ui.users.UserSelectionScreen
import cat.copernic.easytrazamobile.ui.lots.ObrirLotScreen
import cat.copernic.easytrazamobile.ui.lots.TancarLotScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyTrazaMobileTheme(dynamicColor = false) {
                var screen by rememberSaveable { mutableStateOf("users") }
                var ocrText by rememberSaveable { mutableStateOf("") }

                when (screen) {
                    "config" -> ServerConfigScreen(
                        onConfigSaved = { screen = "users" }
                    )

                    "users" -> UserSelectionScreen(
                        onUserSelected = { screen = "menu" },
                        onConfigClick = { screen = "config" }
                    )

                    "menu" -> MenuScreen(
                        onReceiveDeliveryClick = { screen = "rebreAlbara" },
                        onStartLotClick = { screen = "obrirLot" },
                        onCloseLotClick = { screen = "tancarLot" },
                        onLogoutClick = { screen = "users" }
                    )

                    "rebreAlbara" -> RebreAlbaraScreen(
                        ocrText = ocrText,
                        onBackToMenu = { screen = "menu" },
                        onOpenOcrCamera = { screen = "ocrCamera" }
                    )
                    "ocrCamera" -> OcrCameraScreen(
                        onOcrResult = { text ->
                            ocrText = text
                            screen = "rebreAlbara"
                        },
                        onBack = {
                            screen = "rebreAlbara"
                        }
                    )
                    "obrirLot" -> ObrirLotScreen(
                        onBackToMenu = { screen = "menu" }
                    )
                    "tancarLot" -> TancarLotScreen(
                        onBackToMenu = { screen = "menu" }
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun EasyTrazaMobileApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EasyTrazaMobileTheme {
        Greeting("Android")
    }
}