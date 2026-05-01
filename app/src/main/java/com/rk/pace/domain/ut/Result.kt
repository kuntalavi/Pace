package com.rk.pace.domain.ut

sealed interface Result<out D, out E : Error> {
    data class Success<out D, out E : com.rk.pace.domain.ut.Error>(val data: D) : Result<D, E>
    data class Error<out D, out E : com.rk.pace.domain.ut.Error>(val message: E) : Result<D, E>
}