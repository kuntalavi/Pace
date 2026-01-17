package com.rk.pace.domain.repo

interface DataRepo {
    suspend fun deleteUserData(): Boolean?
}