package com.example.lab06

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

class ImageRepo {
    lateinit var uri: Uri

    val SHARED_S = 1
    val PRIVATE_S = 2
    var photo_storage = SHARED_S

    fun setStorage(storage: Int): Boolean {
        if(storage != SHARED_S && storage != PRIVATE_S) {
            return false
        }
        else photo_storage = storage
        return true
    }

    fun changeStorage() {
        if(photo_storage == SHARED_S) photo_storage = PRIVATE_S
        else photo_storage = SHARED_S
    }

    fun getStorage(): Int {
        return photo_storage
    }

    fun getSharedList(): MutableList<GalleryItem>? {
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )
        val selection = MediaStore.Images.Media.DATA + " like ? OR " + MediaStore.Images.Media.DATA + " like ?"
//        val selectionArgs = arrayOf("%Pictures/ForApp/%", "%DCIM/Camera/%")
        val selectionArgs = arrayOf("%Pictures/%")
        val contentResolver: ContentResolver = ctx.contentResolver
        val cursor = contentResolver.query(uri, projection, selection,
            selectionArgs, null)
        sharedStoreList?.clear()

        if(cursor == null) {
            println("There is no cursor in ImageRepo: ln 15")
        } else if(!cursor.moveToFirst()) {
            println("No media in specified folder")
        } else {
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            do {
                var thisId = cursor.getLong(idColumn)
                var thisName = cursor.getString(nameColumn)
                var thisContentUri = ContentUris.withAppendedId(uri, thisId)
                var thisUriPath = thisContentUri.toString()

                sharedStoreList?.add((GalleryItem(thisName, thisUriPath,
                    "No path yet", thisContentUri)))
            } while (cursor.moveToNext())
        }
        return sharedStoreList?.asReversed()
    }

    fun getAppList(): MutableList<GalleryItem>? {
        val dir: File? = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        dir?.listFiles()
        appStoreList?.clear()
        if(dir?.isDirectory == true) {
            var fileList = dir.listFiles()
            if(fileList != null) {
                for(value in fileList) {
                    var filename = value.name
                    if(filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
                        filename.endsWith(".png") || filename.endsWith(".gif")) {
                        val tempUri = FileProvider.getUriForFile(ctx,
                            "${ctx.packageName}.provider", value)
                        appStoreList?.add(GalleryItem(filename, value.toURI().path,
                            value.absolutePath, tempUri))
                    }
                }
            }
        }
        return appStoreList?.asReversed()
    }

    companion object {
        private var INSTANCE: ImageRepo? = null
        private lateinit var ctx: Context

        var sharedStoreList: MutableList<GalleryItem>? = null
        var appStoreList: MutableList<GalleryItem>? = null

        fun getInstance(ctx: Context): ImageRepo {
            if(INSTANCE == null) {
                INSTANCE = ImageRepo()
                sharedStoreList = mutableListOf<GalleryItem>()
                appStoreList = mutableListOf<GalleryItem>()
                this.ctx = ctx
            }
            return INSTANCE as ImageRepo
        }
    }
 }