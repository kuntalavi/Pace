package com.rk.pace.domain.use_case.user

import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.UserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepo: UserRepo
) {
    operator fun invoke(query: String): Flow<List<User>> {
        if (query.isEmpty()) {
            return flowOf(emptyList())
        }
        return userRepo.searchUser(query)

    }
}