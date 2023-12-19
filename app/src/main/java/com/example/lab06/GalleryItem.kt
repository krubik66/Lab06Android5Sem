package com.example.lab06

import android.net.Uri

class GalleryItem {
    var name: String = "defaultName"
    var uripath: String = ""
    var path: String = ""
    var curi: Uri? = null

    constructor(name: String, uripath: String, path: String, curi: Uri) {
        this.name = name
        this.uripath = uripath
        this.path = path
        this.curi = curi
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}