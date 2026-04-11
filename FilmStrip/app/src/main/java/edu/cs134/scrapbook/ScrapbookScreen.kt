package edu.cs134.scrapbook

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ScrapbookScreen(
    viewModel: ScrapbookViewModel,
    modifier: Modifier = Modifier
) {
    val scrapbook by viewModel.photos.collectAsState()
    val textFiles by viewModel.textFiles.collectAsState()
    var photoURI by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val photoId = remember { mutableIntStateOf(0) }

    var showTextFileDialog by remember { mutableStateOf(false) }
    var textFileName by remember { mutableStateOf("") }
    var textFileContent by remember { mutableStateOf("") }

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


    val fileCreateLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("text/plain")
        ) { uri ->
            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use {
                    it.write(textFileContent)
                }
                viewModel.addTextFile(textFileName.ifBlank { "untitled" }, textFileContent, uri)
                textFileName = ""
                textFileContent = ""
            }
        }

    //val fileOpenLauncher =
    //    rememberLauncherForActivityResult(
    //        contract = ActivityResultContracts.OpenDocument()
    //    ) { uri ->
    //        if (uri != null) {
    //            viewModel.loadTextFile(context, uri)
    //        }
    //    }

    if (showTextFileDialog) {
        AlertDialog(
            onDismissRequest = { showTextFileDialog = false },
            title = { Text("New Text File") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = textFileName,
                        onValueChange = { textFileName = it },
                        label = { Text("File name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = textFileContent,
                        onValueChange = { textFileContent = it },
                        label = { Text("Content") },
                        minLines = 4,
                        maxLines = 8
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showTextFileDialog = false
                    fileCreateLauncher.launch("${textFileName.ifBlank { "untitled" }}.txt")
                }) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showTextFileDialog = false
                    textFileName = ""
                    textFileContent = ""
                }) {
                    Text("Cancel")
                }
            }
        )
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

        item {
            TextFileSlot(
                textFile = null,
                onClick = { showTextFileDialog = true }
            )
        }

        item {
            GallerySlot(
                photo = null,
                onPhotoPicked = {}
            )
        }

        itemsIndexed(items = scrapbook) { index, item ->
            ScrapbookSlot(
                photo = item.photo,
                onClick = { /* TODO: add photo delete! */ }
            )
        }

        itemsIndexed(items = textFiles) { index, item ->
            TextFileSlot(
                textFile = item,
                onClick = { /* TODO: add text file delete! */ }
            )
        }
    }
}