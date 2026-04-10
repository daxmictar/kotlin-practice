package edu.cs134.scrapbook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class Photo(
    val photo: Bitmap,
) {
    companion object {
        private var _identifier: Int = 0

        fun getNextId(): Int {
            return _identifier
        }

        fun setIdentifierFloor(id: Int) {
            _identifier = id
        }
    }

    val id: Int = _identifier++
}

class ScrapbookViewModel : ViewModel() {
    /// list of photos stored in the view model
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()

    fun loadPhotos(context: Context) {
        val files = context.externalCacheDir?.listFiles() ?: return
        if (files.isEmpty()) return

        println(files.map { it.name })

        // scans the cache directory for all jpgs prefixed with slot_
        // then filters the string to get the highest value, which is
        // used to set the floor for the next photo ID (so they're not
        // overridden on future loads).
        var maxId: Int = 0
        files.filter{ it.name.contains("slot_") }.forEach {
            // splits into ["slot", "id"], then trims ".jpg"
            try {
                maxId = it.name.split("_")[1].removeSuffix(".jpg").toInt()
            } catch (e: Exception){
                println(e.message)
            }
            val bitmap = BitmapFactory.decodeFile(it.path)
            addPhoto(bitmap)
        }
        Photo.setIdentifierFloor(maxId + 1)
        println("Set maxId to ${Photo.getNextId()}")
    }

    /// Add a photo to the view model.
    fun addPhoto(bitmap: Bitmap) {
        _photos.update { list -> list + Photo(bitmap)}
        println("New photo list: ${_photos.value.map { it.id }}")
    }

    /// Remove a photo from the view model.
    fun removePhoto(id: Int) {
        _photos.update { list -> list.filter{ item -> id != item.id } }
    }

    fun getPhoto(slot: Int): Bitmap? {
        return if (photos.value.isEmpty()) null else photos.value[slot].photo;
    }
}