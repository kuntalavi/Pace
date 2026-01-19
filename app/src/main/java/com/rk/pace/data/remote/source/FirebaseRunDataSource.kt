package com.rk.pace.data.remote.source

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rk.pace.data.remote.dto.RunDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRunDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
//    suspend fun getRunDtoByRunId(runId: String): RunDto? {
//        try {
//            val runWithPathSnapshot = firestore.collection("runs")
//                .document(runId)
//                .get()
//                .await()
//
//            val runDto = runWithPathSnapshot.toObject(RunDto::class.java)
//            return runDto
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//    }
//
//    suspend fun updateRunDto(runDto: RunDto) {
//        try {
//            firestore.collection("runs")
//                .document(runDto.runId)
//                .set(runDto, SetOptions.merge())
//                .await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    suspend fun likeRun(runId: String, currentUserId: String) {
        try {
            firestore.collection("runs")
                .document(runId)
                .update(
                    mapOf(
                        "likes" to FieldValue.increment(1),
                        "likedBy" to FieldValue.arrayUnion(currentUserId)
                    )
                ).await()
        }catch (e: Exception){
            e.printStackTrace()
            // fallback emit to update ui
        }
    }

    suspend fun unlikeRun(runId: String,currentUserId: String){
        try {
            firestore.collection("runs")
                .document(runId)
                .update(
                    mapOf(
                        "likes" to FieldValue.increment(-1),
                        "likedBy" to FieldValue.arrayRemove(currentUserId)
                    )
                )
                .await()
        }catch (e: Exception){
            e.printStackTrace()
            // fallback emit to update ui
        }
    }
}