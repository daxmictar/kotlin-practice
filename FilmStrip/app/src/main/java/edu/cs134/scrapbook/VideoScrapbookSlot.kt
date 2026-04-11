package edu.cs134.scrapbook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun VideoScrapbookSlot(
    onVideoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for capturing video
    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && videoUri != null) {
            onVideoCaptured(videoUri!!)
        }
    }

    Box(modifier = Modifier) {
        if (videoUri == null) {
            Text("Tap to Record Video")
        } else {
            // In a real app, you'd use a VideoPlayer here;
            // for the slot placeholder, we'll show it's "Recorded"
            Text("Video Recorded ✅")
        }
    }
}