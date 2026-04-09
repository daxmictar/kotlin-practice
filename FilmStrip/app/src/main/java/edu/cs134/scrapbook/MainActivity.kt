package edu.cs134.scrapbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import edu.cs134.scrapbook.ui.theme.ScrapbookTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrapbookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val scrapbookVM: ScrapbookViewModel = viewModel()
                    ScrapbookScreen(
                        scrapbookVM,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}