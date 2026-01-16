package com.rk.pace.data.repo

import com.rk.pace.data.remote.source.FirebaseFeedDataSource
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.FeedRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepoImp @Inject constructor(
    private val firebaseFeedDataSource: FirebaseFeedDataSource
): FeedRepo {
    override fun getSocialFeed(): Flow<Result<List<FeedPost>>> {
        return firebaseFeedDataSource.getFeed()
    }

    override fun searchUser(username: String): Flow<List<User>> {
        TODO("Not yet implemented")
    }
}