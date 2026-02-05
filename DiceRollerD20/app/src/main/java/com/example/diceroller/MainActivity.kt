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
import androidx.compose.ui.draw.scale
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
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        6 -> R.drawable.dice6
        7 -> R.drawable.dice7
        8 -> R.drawable.dice8
        9 -> R.drawable.dice9
        10 -> R.drawable.dice10
        11 -> R.drawable.dice11
        12 -> R.drawable.dice12
        13 -> R.drawable.dice13
        14 -> R.drawable.dice14
        15 -> R.drawable.dice15
        16 -> R.drawable.dice16
        17 -> R.drawable.dice17
        18 -> R.drawable.dice18
        19 -> R.drawable.dice19
        else -> R.drawable.dice20
    }
    Image(
        modifier = modifier,
        painter = painterResource(imageResource),
        contentDescription = imageResource.toString()
    )
}

@Composable
fun DiceSum(dice1: Int, dice2: Int, hasRolled: Boolean) {
    val sum = dice1 + dice2
    Text(
        text = if (!hasRolled) " " else "$dice1 + $dice2 = $sum",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
    )
}

@Composable
fun RollButton(onClick: () -> Unit) {
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
            DrawDice(value = dice1, modifier = Modifier.size(200.dp))
            DrawDice(value = dice2, modifier = Modifier.size(200.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        DiceSum(dice1, dice2, hasRolled)
        Spacer(modifier = Modifier.height(16.dp))
        RollButton(onClick = {
            dice1 = (1..20).random()
            dice2 = (1..20).random()
            hasRolled = true
        })
    }
}