package com.example.adaptivehobbies

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaptivehobbies.ui.theme.AdaptiveHobbiesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdaptiveHobbiesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val configuration = LocalConfiguration.current
                    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                    val shortDescription = "I like to play video games in my spare time."
                    val longDescription = "I play a wide variety of games. " +
                            "I really like RPGs and story-based games. One of " +
                            "my favorite games that has come out within the past few years is Baldur's Gate 3."

                    if (!isLandscape) {
                        Column(
                            modifier = Modifier.padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "My Hobby: Gaming",
                                textDecoration = TextDecoration.Underline
                            )
                            HobbyExcerpt(
                                modifier = Modifier,
                                image = painterResource(R.drawable.pc_gamer),
                                description = shortDescription,
                                isExpanded = false
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "My Hobby: Gaming",
                                textDecoration = TextDecoration.Underline
                            )
                            HobbyExcerpt(
                                modifier = Modifier
                                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary)
                                    .fillMaxWidth(),
                                image = painterResource(R.drawable.pc_gamer),
                                description = shortDescription,
                                alternateDescription = "When I'm not playing video games, I'm usually reading books. But I do like to play video games when I have the time.",
                                isExpanded = true
                            )
                            HobbyExcerpt(
                                modifier = Modifier
                                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary),
                                image = painterResource(R.drawable.pc_gamer2),
                                description = longDescription,
                                alternateDescription = "Playing video games has helped me through some hard points in my life. It helps me destress from work, and to connect with friends.",
                                isExpanded = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HobbyExcerpt(
    modifier: Modifier = Modifier,
    image: Painter,
    description: String,
    alternateDescription: String? = null,
    isExpanded: Boolean = false
) {
    var portraitStatus by remember { mutableStateOf(false) }
    val showExpanded = isExpanded && !portraitStatus
    val displayText = if (portraitStatus && alternateDescription != null) alternateDescription else description

    val elements = @Composable {
        Image(
            painter = image,
            contentDescription = image.toString(),
            modifier = Modifier.clickable { portraitStatus = !portraitStatus }
        )
        Text(text = displayText)
    }
    if (!showExpanded) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            elements()
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            elements()
        }
    }
}

@Preview
@Composable
fun HobbyAppConcisePreview() {
    val shortDescription = "I like to play video games in my spare time."
    AdaptiveHobbiesTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Hobby: Gaming",
                textDecoration = TextDecoration.Underline
            )
            HobbyExcerpt(
                modifier = Modifier,
                image = painterResource(R.drawable.pc_gamer),
                description = shortDescription,
                isExpanded = false
            )
        }
    }
}

@Preview(
    widthDp = 720,
    heightDp = 360,
    uiMode = Configuration.ORIENTATION_LANDSCAPE
)
@Composable
fun HobbyAppExpandedPreview() {
    val shortDescription = "I like to play video games in my spare time."
    val longDescription = "I play a wide variety of games. " +
            "I really like RPGs and story-based games. One of " +
            "my favorite games that has come out within the past few years is Baldur's Gate 3."

    AdaptiveHobbiesTheme {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Hobby: Gaming",
                textDecoration = TextDecoration.Underline
            )
            HobbyExcerpt(
                modifier = Modifier
                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                image = painterResource(R.drawable.pc_gamer),
                description = shortDescription,
                alternateDescription = longDescription,
                isExpanded = true
            )
            HobbyExcerpt(
                modifier = Modifier
                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary),
                image = painterResource(R.drawable.pc_gamer2),
                description = longDescription,
                alternateDescription = shortDescription,
                isExpanded = true
            )
        }
    }
}



