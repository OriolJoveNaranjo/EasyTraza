package cat.copernic.easytrazamobile.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val BackgroundWarm = Color(0xFFFFF7ED)
private val PrimaryWarm = Color(0xFF2F6B4F)
private val PrimaryDark = Color(0xFF24513D)
private val TextBrown = Color(0xFF3B240D)

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
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = onReceiveDeliveryClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null
                        )
                    },
                    label = { Text("Rebre") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryWarm,
                        selectedTextColor = PrimaryWarm,
                        indicatorColor = Color(0xFFFFE0B2)
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onStartLotClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null
                        )
                    },
                    label = { Text("Iniciar") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCloseLotClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null
                        )
                    },
                    label = { Text("Tancar") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onLogoutClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ExitToApp,
                            contentDescription = null
                        )
                    },
                    label = { Text("Sortir") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWarm)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EasyTraza",
                    color = PrimaryWarm,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                MenuCard(
                    title = "REBRE ALBARÀ",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    },
                    onClick = onReceiveDeliveryClick
                )

                MenuCard(
                    title = "INICIAR LOT",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    },
                    onClick = onStartLotClick
                )

                MenuCard(
                    title = "TANCAR LOT",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    },
                    onClick = onCloseLotClick
                )

                MenuCard(
                    title = "TANCAR SESSIÓ",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ExitToApp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    },
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
private fun MenuCard(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryWarm
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryDark.copy(alpha = 0.08f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}