package edu.cs134.scrapbook

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ScrapbookSlot(
    photo: Bitmap?,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .border(2.dp, Color.Gray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        if (photo == null) {
            Text("Tap to add photo")
        } else {
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}