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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.graphics.scale

@Composable
fun ScrapbookScreen(viewModel: ScrapbookViewModel,
                    modifier: Modifier = Modifier) {
    var selectedSlot by rememberSaveable { mutableStateOf<Int?>(null) }

    // todo: create a launcher

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        ScrapbookSlot(
            photo = viewModel.photo1,
            onClick = {
                selectedSlot = 1
                // todo: activate the launcher
            }
        )
    }
}