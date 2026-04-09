package edu.cs134.scrapbook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

class ScrapbookViewModel : ViewModel() {
    var photo1 by mutableStateOf<Bitmap?>(null)

    fun setPhoto(slot: Int?, bitmap: Bitmap) {
        when(slot) {
            1 -> photo1 = bitmap
        }
    }
}