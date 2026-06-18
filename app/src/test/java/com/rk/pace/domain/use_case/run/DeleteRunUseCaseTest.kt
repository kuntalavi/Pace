package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.repo.RunRepo
import com.rk.pace.fakeRun
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteRunUseCaseTest {

    private val runRepo: RunRepo = mockk()
    private val useCase = DeleteRunUseCase(runRepo)

    @Test
    fun `calls removeRun on repo with the correct run`() = runTest {

        coEvery { runRepo.removeRun(fakeRun) } returns Unit

        useCase(fakeRun)

        coVerify(
            exactly = 1
        ) { runRepo.removeRun(fakeRun) }

    }

}