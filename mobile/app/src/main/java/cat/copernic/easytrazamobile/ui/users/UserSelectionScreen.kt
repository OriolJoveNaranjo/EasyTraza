package cat.copernic.easytrazamobile.ui.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.easytrazamobile.R
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.components.EasyStatusMessage
import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasyPrimaryDark
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyTextLight
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong
import coil.compose.AsyncImage

/**
 * Screen where the operator selects the active backend user for the mobile session.
 */
@Composable
fun UserSelectionScreen(
    onUserSelected: () -> Unit,
    onConfigClick: () -> Unit,
    viewModel: UserSelectionViewModel = viewModel()
) {
    val usuaris by viewModel.usuaris.collectAsState()
    val message by viewModel.message.collectAsState()
    val baseUrl by viewModel.baseUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startAutoRefreshUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EasyBackground)
            .padding(horizontal = 18.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.common_app_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = EasyTextStrong,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(R.string.users_subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = EasyTextMuted,
            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
        )

        EasyStatusMessage(
            message = message,
            modifier = Modifier.padding(bottom = if (message.isBlank()) 0.dp else 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(usuaris) { usuari ->
                val imageUrl = if (!usuari.foto.isNullOrBlank()) {
                    "$baseUrl/uploads/usuaris/${usuari.foto}"
                } else {
                    "$baseUrl/images/usuaris/fotoPerfil.png"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.88f)
                        .clickable { viewModel.selectUser(usuari.id, onUserSelected) },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = EasySurface),
                    border = BorderStroke(1.dp, EasyBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = usuari.nom,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(22.dp))
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            EasyPrimaryDark.copy(alpha = 0.88f)
                                        )
                                    )
                                )
                        )

                        Text(
                            text = usuari.nom,
                            color = EasyTextLight,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 8.dp, vertical = 14.dp)
                        )
                    }
                }
            }
        }

        EasySecondaryButton(
            text = stringResource(R.string.users_change_server_ip),
            onClick = onConfigClick,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
