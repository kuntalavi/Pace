package com.rk.pace.data.ut

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject


class InternalStorageHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val supabase: SupabaseClient,
    private val auth: FirebaseAuth
) {
    companion object {
        private const val BUCKET_NAME = "user_dp"
    }

    suspend fun syncFirebaseSession(): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser ?: return@withContext false

            val result = user.getIdToken(true).await()
            val token = result.token ?: return@withContext false

            val session = UserSession(
                accessToken = token,
                refreshToken = "",
                expiresIn = 3600,
                tokenType = "Bearer",
                user = null
            )
            supabase.auth.importSession(session)
            Log.d("Supabase", "Session Synced for ${user.uid}")
            true
        } catch (e: Exception) {
            Log.e("Supabase", "Sync failed: ${e.message}")
            false
        }
//        val firebaseUser = auth.currentUser ?: return
//        firebaseUser.getIdToken(true).addOnSuccessListener { result ->
//            val firebaseToken = result.token
//
//            if (firebaseToken != null) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        val session = UserSession(
//                            accessToken = firebaseToken,
//                            refreshToken = "",
//                            expiresIn = 3600,
//                            tokenType = "Bearer",
//                            user = null
//                        )
//                        supabase.auth.importSession(session)
////                         supabase.auth.signInWith(IDToken) {
////                             idToken = firebaseToken
////                             provider = IDToken.pr
////                         }
////                        println("Supabase is now synced with User: ${firebaseUser.uid}")
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
    }

    suspend fun saveGalleryImageToInternalStorage(contentUri: Uri, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(contentUri)

                if (inputStream == null) {
                    Log.e("InternalStorageHelper", "Could not open stream for URI: $contentUri")
                    return@withContext null
                }

                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath
            } catch (e: Exception) {
                Log.e("InternalStorageHelper", "Failed to save gallery image: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun downloadImageToInternalStorage(remoteURL: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                val bytes = URL(remoteURL).readBytes()

                file.writeBytes(bytes)
                Log.d("Supabase", "File saved locally at: ${file.absolutePath}")
                file.absolutePath
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