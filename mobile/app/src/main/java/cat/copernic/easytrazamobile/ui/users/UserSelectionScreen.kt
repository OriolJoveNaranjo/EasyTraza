package cat.copernic.easytrazamobile.ui.users


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserSelectionScreen(
    onUserSelected: () -> Unit,
    onConfigClick: () -> Unit,
    viewModel: UserSelectionViewModel = viewModel()
) {
    val usuaris by viewModel.usuaris.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Qui ets?",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
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
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 32.dp)
        ) {
            items(usuaris) { usuari ->
                Card(
                    modifier = Modifier
                        .size(width = 150.dp, height = 130.dp)
                        .clickable {
                            viewModel.selectUser(usuari.id, onUserSelected)
                        },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("👤", style = MaterialTheme.typography.headlineLarge)

                        Text(
                            text = usuari.nom,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Button(onClick = onConfigClick) {
            Text("Canviar IP servidor")
        }
    }
}