package cat.copernic.easytrazamobile.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cat.copernic.easytrazamobile.ui.theme.EasyAccentSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasySidebar
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyTextLight
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong

@Composable
fun MenuScreen(
    onReceiveDeliveryClick: () -> Unit = {},
    onStartLotClick: () -> Unit = {},
    onCloseLotClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = EasySurface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = onReceiveDeliveryClick,
                    icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = null) },
                    label = { Text("Rebre") },
                    colors = menuNavColors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onStartLotClick,
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = null) },
                    label = { Text("Obrir") },
                    colors = menuNavColors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onCloseLotClick,
                    icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
                    label = { Text("Tancar") },
                    colors = menuNavColors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onLogoutClick,
                    icon = { Icon(Icons.Outlined.ExitToApp, contentDescription = null) },
                    label = { Text("Sortir") },
                    colors = menuNavColors()
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EasyBackground)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EasySidebar)
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "EasyTraza",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gestió ràpida de recepcions i lots",
                        color = EasyTextLight.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(
                    title = "Rebre albarà",
                    subtitle = "OCR, dades i alta de lots",
                    icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp)) },
                    onClick = onReceiveDeliveryClick
                )
                MenuCard(
                    title = "Obrir lot",
                    subtitle = "Iniciar consum de matèria primera",
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp)) },
                    onClick = onStartLotClick
                )
                MenuCard(
                    title = "Tancar lot",
                    subtitle = "Finalitzar un lot obert",
                    icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp)) },
                    onClick = onCloseLotClick
                )
                MenuCard(
                    title = "Tancar sessió",
                    subtitle = "Tornar a seleccionar usuari",
                    icon = { Icon(Icons.Outlined.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp)) },
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
private fun menuNavColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = EasyPrimary,
    selectedTextColor = EasyPrimary,
    unselectedIconColor = EasyTextMuted,
    unselectedTextColor = EasyTextMuted,
    indicatorColor = EasyAccentSoft
)

@Composable
private fun MenuCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = EasySidebar),
        border = androidx.compose.foundation.BorderStroke(1.dp, EasyBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.size(18.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = EasyTextLight.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
