package com.example.lab06

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ImageRepo {
    lateinit var uri: Uri

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
        return appStoreList
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