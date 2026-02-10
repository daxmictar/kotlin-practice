package com.example.adaptivelayouts

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaptivelayouts.ui.theme.AdaptiveLayoutsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdaptiveLayoutsTheme {
            }
        }
    }
}

val appTextColor = Color.White

fun getAnswer(): String {
    val answers = listOf(
        "It is certain",
        "It is decidedly so",
        "Without a doubt",
        "Yes definitely"
    )
    return answers.random()
}

@Composable
fun Magic8BallImage(modifier: Modifier = Modifier, hasBeenAsked: Boolean = false) {
    val eightBallImage = if (hasBeenAsked) R.drawable._ball2 else R.drawable._ball1
    val imageResource = painterResource(eightBallImage)
    Box(modifier = modifier) {
        Image(
            modifier = modifier,
            painter = imageResource,
            contentDescription = imageResource.toString()
        )
        if (hasBeenAsked) {
            Text(
                text = getAnswer(),
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun Magic8BallTextField(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    hasAnswer: MutableState<Boolean>,
    asked: String?
) {
    Text(
        text = if (!hasAnswer.value) "Ask a question!" else "You asked: $asked",
        color = appTextColor,
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
    )
    OutlinedTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = {
            if (it.isNotEmpty()) {
                hasAnswer.value = false
            }
            text.value = it
        },
        label = { Text("Enter question...") }
    )
}

@Composable
fun Magic8BallApp(
    portrait: Boolean = true,
) {
    val elements = @Composable {
        val questionState = remember { mutableStateOf("") }
        val askedState = remember { mutableStateOf("") }
        val hasAnswer = remember { mutableStateOf(false) }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "Magic 8 Ball",
                color = appTextColor,
                fontSize = 24.sp
            )
            Magic8BallImage(
                modifier = Modifier.size(300.dp, 300.dp),
                hasBeenAsked = hasAnswer.value
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Magic8BallTextField(
                modifier = Modifier,
                text = questionState,
                hasAnswer = hasAnswer,
                asked = askedState.value
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    if (questionState.value.isNotEmpty()) {
                        askedState.value = questionState.value
                        questionState.value = ""
                        hasAnswer.value = !hasAnswer.value
                    }
                }
            ) {
                Text("Ask")
            }
        }
    }

    if (portrait) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            elements()
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            elements()
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    Magic8BallApp(portrait = true)
}

@Preview(
    widthDp = 720,
    heightDp = 360,
    uiMode = Configuration.ORIENTATION_LANDSCAPE
)
@Composable
fun AppLandscapePreview() {
    Magic8BallApp(portrait = false)
}
