package com.rk.pace.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class BitmapH @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun saveBitmap(bitmap: Bitmap, nameP: String): Uri? {

        val name = "${nameP}${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, name)

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

/*
capture bitmap -> save bitmap to storage -> get bitmap uri -> save run with bitmap uri in room
 */