package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
                Surface(color = Color.DarkGray) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DrawDice(modifier: Modifier = Modifier, value: Int) {
    val imageResource = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    Image(
        modifier = Modifier,
        painter = painterResource(imageResource),
        contentDescription = imageResource.toString()
    )
}

@Composable
fun RollButtonWithSum(dice1: Int, dice2: Int, hasRolled: Boolean, onClick: () -> Unit) {
    val sum = dice1 + dice2
    Text(
        text = if (!hasRolled) " " else "$dice1 + $dice2 = $sum",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = onClick) {
        Text(stringResource(R.string.roll))
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var dice1 by remember { mutableStateOf(1) }
    var dice2 by remember { mutableStateOf(1) }
    var hasRolled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            DrawDice(value = dice1, modifier = Modifier.weight(1f))
            DrawDice(value = dice2, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        RollButtonWithSum(dice1, dice2, hasRolled, onClick = {
            dice1 = (1..6).random()
            dice2 = (1..6).random()
            hasRolled = true
        })
    }
}