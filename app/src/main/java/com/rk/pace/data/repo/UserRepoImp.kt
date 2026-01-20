package com.rk.pace.data.repo

import android.net.Uri
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toDto
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.data.remote.source.FirebaseUserDataSource
import com.rk.pace.data.room.dao.UserDao
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.UserRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UserRepoImp @Inject constructor(
    private val internalStorageHelper: InternalStorageHelper,
    private val firebaseUserDataSource: FirebaseUserDataSource,
    private val userDao: UserDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepo {

    override suspend fun getUserProfile(userId: String): Result<User> =
        withContext(ioDispatcher) {
            val userDto = firebaseUserDataSource.getUserById(userId)

            if (userDto != null) {
                Result.success(userDto.toDomain(photoURI = "")) //
            } else {
                Result.failure(Exception("MyProfile Not Found"))
            }
        }

    override suspend fun getLikedByUsersByUsersIds(usersIds: List<String>): Result<List<User>> =
        withContext(ioDispatcher) {
            val usersDto = firebaseUserDataSource.getUsersByIds(userIds = usersIds)

            val users = usersDto.map { it.toDomain(photoURI = "") }
            Result.success(users)
        }

    override suspend fun updateProfile(user: User) = withContext(ioDispatcher) {
        val userEntity = user.toEntity()
        userDao.updateUser(userEntity)

        // work manager required here
        firebaseUserDataSource.updateUserProfile(
            user.toDto(), user.photoURI
        )
        return@withContext
    }

    override suspend fun getMyProfile(): Result<User> = withContext(ioDispatcher) {
        try {
            val userEntity = userDao.getUser()

            if (userEntity != null) {
                return@withContext Result.success(userEntity.toDomain())
            }

            val currentUserId = firebaseUserDataSource.getCurrentUserId()
                ?: return@withContext Result.failure(Exception("MyProfile Not logged In"))

            val userDto = firebaseUserDataSource.getUserById(currentUserId)
            if (userDto != null) {
                var photoURI: String? = null
                if (userDto.photoURL != null) {
                    val localPath = internalStorageHelper.downloadSupabaseImageToInternalStorage(
                        userDto.photoURL,
                        userDto.userId
                    )
                    if (localPath != null) {
                        photoURI = Uri.fromFile(File(localPath)).toString()
                    }
                }
                //
                userDao.insertUser(
                    (userDto.toDomain(photoURI = photoURI)).toEntity()
                )
                Result.success(userDto.toDomain(photoURI = photoURI))
            } else {
                Result.failure(Exception("MyProfile Not Found"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchUser(query: String): Flow<List<User>> {
        return firebaseUserDataSource.searchUser(query).map { dtos ->
            dtos.map { it.toDomain(photoURI = "") }
        }
    }

    override suspend fun observeUserProfile(userId: String): Flow<Result<User>> {
        TODO("Not yet implemented")
    }
}