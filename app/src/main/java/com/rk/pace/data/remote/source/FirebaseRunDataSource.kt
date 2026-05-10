package com.rk.pace.data.remote.source

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRunDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getRunsByUserIds(userIds: List<String?>): List<RunDto> =
        withContext(ioDispatcher) {
            return@withContext try {

                if (userIds.isEmpty()) return@withContext emptyList()

                val runsDtos = userIds
                    .chunked(30)
                    .map { batch ->
                        coroutineScope {
                            async {
                                firestore
                                    .collection("runs")
                                    .whereIn("userId", batch)
                                    .orderBy("timestamp", Query.Direction.DESCENDING)
                                    .limit(20)
                                    .get()
                                    .await()
                                    .documents
                                    .mapNotNull {
                                        it.toObject(RunDto::class.java)
                                    }
                            }
                        }
                    }
                    .awaitAll()
                    .flatten()

                runsDtos
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun getUserRuns(userId: String): List<RunDto> =
        withContext(ioDispatcher) {
            return@withContext try {
                val runsWithPathSnapshot = firestore
                    .collection("runs")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val runsDtos = runsWithPathSnapshot.map { document ->
                    document.toObject(RunDto::class.java)
                }
                runsDtos
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun getRunById(runId: String): RunDto? =
        withContext(ioDispatcher) {
            return@withContext try {
                val runWithPathSnapshot = firestore
                    .collection("runs")
                    .document(runId)
                    .get()
                    .await()

                val runDto = runWithPathSnapshot.toObject(RunDto::class.java)
                runDto
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

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
        } catch (e: Exception) {
            e.printStackTrace()
            // fallback emit to update ui
        }
    }

    suspend fun unlikeRun(runId: String, currentUserId: String) {
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
        } catch (e: Exception) {
            e.printStackTrace()
            // fallback emit to update ui
        }
    }
}