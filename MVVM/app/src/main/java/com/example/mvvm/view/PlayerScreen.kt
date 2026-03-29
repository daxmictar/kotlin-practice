package com.example.mvvm.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.AsyncImage
import com.example.mvvm.ui.theme.MVVMTheme
import com.example.mvvm.viewmodel.PlayerViewModel

@Composable
fun Image(url: String, flip: Boolean = false) {
    AsyncImage(
        model = url,
        contentDescription = "Player Image",
        modifier = if (flip) Modifier.graphicsLayer { scaleX = -1f; } else Modifier,
        colorFilter = if (flip) ColorFilter.tint(Color.Black) else null
    )
}

@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val player by playerViewModel.player.collectAsState()

    Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(16.dp),
       verticalArrangement =  Arrangement.spacedBy(10.dp),
       horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${player.name} vs. Enemy")

        // Image source: https://pixelartmaker.com/art/98743367cf0222c
        Row() {
            Image(
                url = "https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/98743367cf0222c.png",
                flip = false
            )
            Image(
                url = "https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/98743367cf0222c.png",
                flip = true
            )
        }

        Text(text = playerViewModel.getTitle())

        Text(text = playerViewModel.getHealthStatus())

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "❤ ${player.health} / 100")
            Text(text = "⚡ ${player.stamina} / 100")
            Text(text = "⚔ ${player.attackPower}")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(onClick = {
                playerViewModel.takeDamage(5)
            }) { Text(text = "Take Damage") }
            Button(onClick = {
                playerViewModel.levelUp()
            }) { Text(text = "Level Up") }
            Button(onClick = {
                playerViewModel.rest()
            }) { Text(text = "Rest") }
        }
    }
}
