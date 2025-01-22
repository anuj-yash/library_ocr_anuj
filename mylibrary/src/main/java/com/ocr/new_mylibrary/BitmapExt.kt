package com.ocr.new_mylibrary

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.IOException

fun Bitmap.toUri(context: Context): Uri? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

    val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val resolver = context.contentResolver
    val uri = resolver.insert(collection, values) ?: return null

    try {
        val outputStream = resolver.openOutputStream(uri) ?: return null
        compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        resolver.delete(uri, null, null)
        return null
    }

    return uri
}