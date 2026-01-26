package com.rk.pace.data.ut

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rk.pace.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject


class InternalStorageHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun saveGalleryImageToInternalStorage(contentUri: Uri, userId: String): String? {
        return withContext(ioDispatcher) {
            try {
                val directory = File(context.filesDir, userId)

                if (!directory.exists()){
                    directory.mkdirs()
                } else {
                    directory.listFiles()?.forEach { file ->
                        file.delete()
                    }
                }

                val newFileName = "${System.currentTimeMillis()}.png"
                val newFile = File(directory, newFileName)

                val inputStream: InputStream? = context.contentResolver.openInputStream(contentUri)

                if (inputStream == null) {
                    Log.e("DP", "savingGalleryImageToInternalStorage: Could not open stream for selected URI: $contentUri")
                    return@withContext null
                }

                val outputStream = FileOutputStream(newFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                newFile.absolutePath
            } catch (e: Exception) {
                Log.e("DP", "Failed to save gallery image: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun downloadSupabaseImageToInternalStorage(
        supabaseURL: String,
        userId: String
    ): String? {
        return withContext(ioDispatcher) {
            try {
                val directory = File(context.filesDir, userId)

                if (!directory.exists()){
                    directory.mkdirs()
                }else{
                    directory.listFiles()?.forEach { file ->
                        file.delete()
                    }
                }

                val newFileName = "${System.currentTimeMillis()}.png"
                val newFile = File(directory, newFileName)

                val bytes = URL(supabaseURL).readBytes()

                newFile.writeBytes(bytes)
                Log.d("Supabase", "File s locally at: ${newFile.absolutePath}")
                newFile.absolutePath
            } catch (e: Exception) {
                Log.e("Supabase", "Download Failed: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

}