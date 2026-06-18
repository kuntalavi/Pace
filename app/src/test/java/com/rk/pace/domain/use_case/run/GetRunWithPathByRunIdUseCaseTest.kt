package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.repo.RunRepo
import com.rk.pace.fakeRunWithPath
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetRunWithPathByRunIdUseCaseTest {

    private val runRepo: RunRepo = mockk()
    private val useCase = GetRunWithPathByRunIdUseCase(runRepo)

    @Test
    fun `returns runWithPath from runRepo when run exists`() = runTest {

        coEvery { runRepo.getRunWithPathByRunId("run-123") } returns fakeRunWithPath

        val result = useCase("run-123")

        assertEquals(
            fakeRunWithPath,
            result
        )
        coVerify(
            exactly = 1
        ) { runRepo.getRunWithPathByRunId("run-123") }

    }

    @Test
    fun `returns null from repo when run does not exist`() = runTest {

        coEvery { runRepo.getRunWithPathByRunId("missing-id") } returns null

        val result = useCase("missing-id")

        assertNull(
            result
        )
        coVerify(
            exactly = 1
        ) { runRepo.getRunWithPathByRunId("missing-id") }

    }

}