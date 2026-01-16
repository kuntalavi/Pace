package com.rk.pace.domain.use_case.feed

import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.FeedRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val repo: FeedRepo
) {
    operator fun invoke(username: String): Flow<List<User>> {
        return repo.searchUser(username)
    }
}