package edu.cs134.scrapbook

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.graphics.scale

fun getContextualUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "shot_",
        ".jpg",
        context.externalCacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}

@Composable
fun ScrapbookScreen(
    viewModel: ScrapbookViewModel,
    modifier: Modifier = Modifier
) {
    val scrapbook by viewModel.photos.collectAsState()
    var photoURI by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val photoId = remember { mutableIntStateOf(0) }

    // For reclearing the cache
    // context.externalCacheDir?.deleteRecursively()

    // Load in all of the photos (if available), otherwise start with an empty list
    if (scrapbook.isEmpty()) {
        println("Loading photos...")
        viewModel.loadPhotos(context)
        photoId.intValue = Photo.getNextId()
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) {
            success ->
            if (success && photoURI != null) {
                val inputStream = context.contentResolver.openInputStream(photoURI!!)
                val fullBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                println("Adding photo with photoId ${photoId.intValue}")
                viewModel.addPhoto(fullBitmap)
                // workaround without refactoring the way photos are taken
                photoId.intValue = Photo.getNextId()

                photoURI = null
            }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            // always render an empty slot so the user can click to add more
            ScrapbookSlot(
                photo = null,
                onClick = {
                    val storageDir = context.externalCacheDir
                    val imageFile = File(storageDir, "slot_${photoId.intValue}.jpg")
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )
                    photoURI = uri

                    cameraLauncher.launch(photoURI!!)
                }
            )
        }

        itemsIndexed(items = scrapbook) { index, item ->
            ScrapbookSlot(
                photo = item.photo,
                onClick = { /* TODO: add photo delete! */ }
            )
        }
    }
}