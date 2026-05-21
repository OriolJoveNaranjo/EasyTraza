package cat.copernic.easytrazamobile.ui.users

import cat.copernic.easytrazamobile.ui.theme.EasyBackground
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyPrimary
import cat.copernic.easytrazamobile.ui.theme.EasyPrimaryDark
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyTextMuted
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "EasyTraza",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = EasyPrimaryDark,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Qui fa el seguiment avui?",
            style = MaterialTheme.typography.titleMedium,
            color = EasyTextMuted,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
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
                        .aspectRatio(0.82f)
                        .clickable {
                            viewModel.selectUser(usuari.id, onUserSelected)
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = usuari.nom,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xAA000000)
                                        )
                                    )
                                )
                        )

                        Text(
                            text = usuari.nom,
                            color = EasySurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 14.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = onConfigClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Canviar IP servidor")
        }
    }
}