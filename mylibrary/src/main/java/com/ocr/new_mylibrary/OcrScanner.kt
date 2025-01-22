package com.ocr.new_mylibrary

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrScanner() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var scannedText: String? = null

    interface TextRecognitionCallback {
        fun onTextRecognized(text: String)
        fun onError(exception: Exception)
    }

    fun recognizeTextFromImage(
        activity: Activity,
        imageUri: Uri,
        callback: TextRecognitionCallback
    ) {
        activity.runOnUiThread {
            if (imageUri == null) {
                callback.onError(IllegalArgumentException("Image URI cannot be null"))
                return@runOnUiThread
            }

            try {
                val bitmap = imageUri.toBitmap(activity)
                val image = InputImage.fromBitmap(bitmap, 0)

                recognizer.process(image)
                    .addOnSuccessListener { result ->
                        callback.onTextRecognized(result.text)
                    }
                    .addOnFailureListener { exception ->
                        callback.onError(exception)
                    }
            } catch (e: Resources.NotFoundException) {
                callback.onError(e)
            }
        }
    }
}

// Helper function to convert Uri to Bitmap (replace with your preferred method)
fun Uri.toBitmap(activity: Activity): Bitmap {
    // Implement your logic to convert Uri to Bitmap here
    // For example:
    val inputStream = activity.contentResolver.openInputStream(this)
    return BitmapFactory.decodeStream(inputStream)
}
