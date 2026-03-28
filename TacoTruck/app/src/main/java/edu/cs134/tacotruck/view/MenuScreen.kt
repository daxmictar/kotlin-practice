package edu.cs134.tacotruck.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cs134.tacotruck.model.MenuItem
import edu.cs134.tacotruck.viewmodel.TacoTruckViewModel

@Composable
fun MenuScreen(
    viewModel: TacoTruckViewModel,
    modifier: Modifier
) {

    // Taco Truck menu items
    val taco = MenuItem("🌮 Taco", 3.0)
    val burrito = MenuItem("🌯 Burrito", 7.0)
    val guac = MenuItem("🥑 Guacamole", 2.5)
    val horchata = MenuItem("🥛 Horchata", 2.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("🌮 Taco Truck Menu", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { viewModel.addItem(taco) }) {
            Text("Add Taco ($3)")
        }

        Button(onClick = { viewModel.addItem(burrito) }) {
            Text("Add Burrito ($7)")
        }

        Button(onClick = { viewModel.addItem(guac) }) {
            Text("Add Guacamole ($2.5)")
        }

        Button(onClick = { viewModel.addItem(horchata) }) {
            Text("Add Horchata ($2)")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}