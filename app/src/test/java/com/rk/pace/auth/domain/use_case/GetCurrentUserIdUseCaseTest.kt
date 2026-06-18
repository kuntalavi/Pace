package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetCurrentUserIdUseCaseTest {

    private val authRepo: AuthRepo = mockk()
    private val useCase = GetCurrentUserIdUseCase(authRepo)

    @Test
    fun `returns currentUserId from authRepo when user is signed in`() {

        every { authRepo.currentUserId } returns "user-456"

        val result = useCase()

        assertEquals(
            "user-456",
            result
        )

    }

    @Test
    fun `returns null when no user is signed in`() {

        every { authRepo.currentUserId } returns null

        val result = useCase()

        assertNull(
            result
        )

    }

}