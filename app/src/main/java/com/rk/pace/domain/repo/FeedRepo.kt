package com.rk.pace.domain.repo

import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FeedRepo {

    fun getSocialFeed(): Flow<Result<List<FeedPost>>>
//    fun pullToRefresh()
    fun searchUser(username: String): Flow<List<User>>
}