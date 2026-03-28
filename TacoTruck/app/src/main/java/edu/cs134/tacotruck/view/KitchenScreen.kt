package edu.cs134.tacotruck.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cs134.tacotruck.viewmodel.TacoTruckViewModel

@Composable
fun KitchenScreen(
    viewModel: TacoTruckViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val status = viewModel.getStatus()
        Text(status)
    }
}