package com.example.micromvp

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.micromvp.ui.theme.MicroMVPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MicroMVPTheme {
                Surface(color = Color.DarkGray) {
                    MicroMvpApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun MicroMvpApp() {
    ButtonsWithCounter(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun IncrementButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text = stringResource(R.string.incrementButton))
    }
}

@Composable
fun ResetButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text = stringResource(R.string.resetButton))
    }
}

val BUTTON_SIZE = Modifier.size(width = 100.dp, height = 40.dp)

@Composable
fun ButtonsWithCounter(modifier: Modifier = Modifier) {
    var clicks by remember { mutableIntStateOf(0) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val emoji = when {
            clicks in 1..4 -> "\uD83D\uDE10"
            clicks in 5..9 -> "\uD83D\uDE42"
            clicks in 10..14 -> "\uD83D\uDE0A"
            clicks >= 15 -> "☺\uFE0F"
            else -> "\uD83D\uDE14"
        }
        Text(
            text = emoji,
            fontSize = 50.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Clicks: $clicks",
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.5.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            IncrementButton(
                modifier = BUTTON_SIZE,
                onClick = { clicks += 1 }
            )
            Spacer(Modifier.width(10.dp))
            ResetButton(
                modifier = BUTTON_SIZE,
                onClick = { clicks = 0 }
            )
        }
    }
}
