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
//    companion object {
//        private const val BUCKET_NAME = "user_dp"
//    }

//    suspend fun syncFirebaseAuthToSupabase(user: FirebaseUser): Boolean =
//        withContext(ioDispatcher) {
//            try {
//                val result = user.getIdToken(true).await()
//                val token = result.token ?: return@withContext false
//
//                val session = UserSession(
//                    accessToken = token,
//                    refreshToken = "",
//                    expiresIn = 3600,
//                    tokenType = "Bearer",
//                    user = null
//                )
//                supabase.auth.importSession(session)
//                Log.d("Supabase", "Session Synced for ${user.uid}")
//                true
//            } catch (e: Exception) {
//                Log.e("Supabase", "Sync failed: ${e.message}")
//                false
//            }
//        }

    // user_dp/uuid/image

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

//                val file = File(context.filesDir, fileName)
//
//                if (file.exists()){
//                    file.delete()
//                }
//
//                file.parentFile?.let {
//                    if (!it.exists()){
//                        val created = it.mkdirs()
//                        Log.d("DP", "Creating directory ${it.absolutePath}: $created")
//                    }
//                }
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
//                val file = File(context.filesDir, fileName)
//
//                file.parentFile?.mkdirs()
//
//                if (file.exists()) {
//                    file.delete()
//                    Log.d("Supabase", "Deleted old file: ${file.absolutePath}")
//                }

                val bytes = URL(supabaseURL).readBytes()

                newFile.writeBytes(bytes)
                Log.d("Supabase", "File saved locally at: ${newFile.absolutePath}")
                newFile.absolutePath
            } catch (e: Exception) {
                Log.e("Supabase", "Download Failed: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

}

//
//                val url = URL(remoteURL)
//                val connection = url.openConnection()
//                connection.connect()
//                val inputStream = connection.getInputStream()
//
//                val outputStream = FileOutputStream(file)
//
//                val buffer = ByteArray(1024)
//                var len: Int
//                while (inputStream.read(buffer).also { len = it } > 0) {
//                    outputStream.write(buffer, 0, len)
//                }
//
//                outputStream.flush()
//                outputStream.close()
//                inputStream.close()

//    fun copyUriToInternalStorage()

//    fun saveNewRunBitmap(bitmap: Bitmap): Uri? {
//
//        val name = "R_${System.currentTimeMillis()}.png"
//        val file = File(context.filesDir, name)
//
//        return try {
//            FileOutputStream(file).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//            }
//            Uri.fromFile(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//    suspend fun saveFirebaseRunBitmap(bitmapURL: String, name: String): Uri? =
//        withContext(Dispatchers.IO) {
//            val fName = if (name.endsWith(".png") || name.endsWith(".jpg")) {
//                name
//            } else {
//                "$name.png"
//            }
//            val file = File(context.filesDir, fName)
//
//            return@withContext try {
//                val url = URL(bitmapURL)
//                val connection = url.openConnection()
//                connection.connect()
//
//                connection.getInputStream().use { input ->
//                    FileOutputStream(file).use { output ->
//                        input.copyTo(output)
//                    }
//                }
//                Uri.fromFile(file)
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
//        }