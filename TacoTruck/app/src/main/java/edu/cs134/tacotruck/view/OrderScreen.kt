package edu.cs134.tacotruck.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cs134.tacotruck.viewmodel.TacoTruckViewModel

@Composable
fun OrderScreen(
    viewModel: TacoTruckViewModel,
    modifier: Modifier
) {

    val order by viewModel.order.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("🧾 Current Order", style = MaterialTheme.typography.headlineMedium)

        order.forEach {
            Text("${it.name} - $${it.price}")
        }

        Text("Total: $${viewModel.getTotal()}")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { viewModel.clearOrder() }) {
            Text("Clear Order")
        }
    }
}