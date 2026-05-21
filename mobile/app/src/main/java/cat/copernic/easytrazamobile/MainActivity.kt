package cat.copernic.easytrazamobile

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cat.copernic.easytrazamobile.ui.albarans.OcrCameraScreen
import cat.copernic.easytrazamobile.ui.albarans.RebreAlbaraScreen
import cat.copernic.easytrazamobile.ui.config.ServerConfigScreen
import cat.copernic.easytrazamobile.ui.lots.ObrirLotScreen
import cat.copernic.easytrazamobile.ui.lots.TancarLotScreen
import cat.copernic.easytrazamobile.ui.menu.MenuScreen
import cat.copernic.easytrazamobile.ui.theme.EasyTrazaMobileTheme
import cat.copernic.easytrazamobile.ui.users.UserSelectionScreen

/**
 * Main entry point of the EasyTraza mobile application.
 *
 * This activity keeps the app navigation simple by storing the current screen in Compose
 * state. The mobile workflow is intentionally small: select user, receive delivery notes,
 * open lots and close lots.
 */
class MainActivity : ComponentActivity() {

    /** Creates the Compose UI and initializes the mobile navigation flow. */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyTrazaMobileTheme(dynamicColor = false) {
                var screen by rememberSaveable { mutableStateOf(Screen.Users.route) }
                var ocrText by rememberSaveable { mutableStateOf("") }

                when (screen) {
                    Screen.Config.route -> ServerConfigScreen(
                        onConfigSaved = { screen = Screen.Users.route }
                    )

                    Screen.Users.route -> UserSelectionScreen(
                        onUserSelected = { screen = Screen.Menu.route },
                        onConfigClick = { screen = Screen.Config.route }
                    )

                    Screen.Menu.route -> MenuScreen(
                        onReceiveDeliveryClick = { screen = Screen.ReceiveDelivery.route },
                        onStartLotClick = { screen = Screen.OpenLot.route },
                        onCloseLotClick = { screen = Screen.CloseLot.route },
                        onLogoutClick = { screen = Screen.Users.route }
                    )

                    Screen.ReceiveDelivery.route -> RebreAlbaraScreen(
                        ocrText = ocrText,
                        onBackToMenu = { screen = Screen.Menu.route },
                        onOpenOcrCamera = { screen = Screen.OcrCamera.route }
                    )

                    Screen.OcrCamera.route -> OcrCameraScreen(
                        onOcrResult = { text ->
                            ocrText = text
                            screen = Screen.ReceiveDelivery.route
                        },
                        onBack = { screen = Screen.ReceiveDelivery.route }
                    )

                    Screen.OpenLot.route -> ObrirLotScreen(
                        onBackToMenu = { screen = Screen.Menu.route }
                    )

                    Screen.CloseLot.route -> TancarLotScreen(
                        onBackToMenu = { screen = Screen.Menu.route }
                    )
                }
            }
        }
    }
}

/** Internal routes used by the simple Compose navigation state. */
private enum class Screen(val route: String) {
    Config("config"),
    Users("users"),
    Menu("menu"),
    ReceiveDelivery("rebreAlbara"),
    OcrCamera("ocrCamera"),
    OpenLot("obrirLot"),
    CloseLot("tancarLot")
}
