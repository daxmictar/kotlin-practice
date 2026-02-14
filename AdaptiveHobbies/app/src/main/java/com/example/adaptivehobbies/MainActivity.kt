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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
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
                    HobbyApp(
                        modifier = Modifier.padding(innerPadding),
                        info = HobbyInfo(
                            shortDescription = "I like to play video games in my spare time.",
                            longDescription = "I play a wide variety of games. " +
                                    "I really like RPGs and story-based games. One of " +
                                    "my favorite games that has come out within the past few years is Baldur's Gate 3.",
                            teaserPhoto = painterResource(R.drawable.pc_gamer),
                            detailedPhoto = null
                        )
                    )
                }
            }
        }
    }
}

data class HobbyInfo(
    val shortDescription: String,
    val longDescription: String,
    val teaserPhoto: Painter,
    val detailedPhoto: Painter?
)

@Composable
fun HobbyExcerpt(
    modifier: Modifier = Modifier,
    image: Painter,
    description: String,
    isExpanded: Boolean = false
) {
    val elements = @Composable {
        Image(
            painter = image,
            contentDescription = image.toString()
        )
        Text(text = description)
    }
    if (!isExpanded) {
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
                alignment = Alignment.CenterHorizontally
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
                isExpanded = true
            )
            HobbyExcerpt(
                modifier = Modifier
                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary),
                image = painterResource(R.drawable.pc_gamer),
                description = longDescription,
                isExpanded = true
            )
        }
    }
}



