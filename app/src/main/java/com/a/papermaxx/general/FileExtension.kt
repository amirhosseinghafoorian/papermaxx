package com.a.papermaxx.general

import android.net.Uri
import android.webkit.MimeTypeMap
import com.a.papermaxx.app.MyApp

class FileExtension {

    fun getFileExtension(uri: Uri): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(MyApp.publicApp.contentResolver.getType(uri))
    }

}