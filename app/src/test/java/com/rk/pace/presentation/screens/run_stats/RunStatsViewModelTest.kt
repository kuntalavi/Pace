package com.rk.pace.presentation.screens.run_stats

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.domain.use_case.run.DeleteRunUseCase
import com.rk.pace.domain.use_case.run.GetRunWithPathByRunIdUseCase
import com.rk.pace.fakePathPoints
import com.rk.pace.fakeRun
import com.rk.pace.fakeRunWithPath
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RunStatsViewModelTest {

    private lateinit var getRunWithPathByRunIdUseCase: GetRunWithPathByRunIdUseCase
    private lateinit var deleteRunUseCase: DeleteRunUseCase
    private lateinit var getCurrentUserIdUseCase: GetCurrentUserIdUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)

        getRunWithPathByRunIdUseCase = mockk()
        deleteRunUseCase = mockk()
        getCurrentUserIdUseCase = mockk()

        every { getCurrentUserIdUseCase() } returns "user-456"

    }

    @After
    fun down() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(
        userId: String = "user-456",
        runId: String = "run-123"
    ): RunStatsViewModel {

        return RunStatsViewModel(
            getRunWithPathByRunIdUseCase = getRunWithPathByRunIdUseCase,
            deleteRunUseCase = deleteRunUseCase,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "userId" to userId,
                    "runId" to runId
                )
            )
        )

    }


    @Test
    fun `initial state has load true before data arrives`() {

        coEvery { getRunWithPathByRunIdUseCase(any()) } returns fakeRunWithPath

        val viewModel = buildViewModel()

        assertEquals(
            true,
            viewModel.state.value.load
        )
        assertNull(
            viewModel.state.value.data
        )
        assertNull(
            viewModel.state.value.error
        )

    }

    @Test
    fun `state has data populated after valid runId loads successfully`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase("run-123") } returns fakeRunWithPath

        val viewModel = buildViewModel(runId = "run-123")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals(
            false,
            state.load
        )
        assertNull(
            state.error
        )
        assertNotNull(
            state.data
        )
        assertEquals(
            fakeRun,
            state.data?.run
        )
        assertEquals(
            fakePathPoints,
            state.data?.path
        )

    }

    @Test
    fun `isCurrentUser is true when savedState userId matches signed in user`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase(any()) } returns fakeRunWithPath
        every { getCurrentUserIdUseCase() } returns "user-456"

        val viewModel = buildViewModel(userId = "user-456")
        advanceUntilIdle()

        assertTrue(
            viewModel.state.value.isCurrentUser
        )

    }

    @Test
    fun `isCurrentUser is false when savedState userId differs from signed in user`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase(any()) } returns fakeRunWithPath
        every { getCurrentUserIdUseCase() } returns "user-456"

        val viewModel = buildViewModel(userId = "other-user-789")
        advanceUntilIdle()

        assertEquals(
            false,
            viewModel.state.value.isCurrentUser
        )

    }

    @Test
    fun `state shows error when runId is empty`() = runTest {

        val viewModel = buildViewModel(runId = "")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals(
            false,
            state.load
        )
        assertEquals(
            "",
            state.error
        )
        assertNull(
            state.data
        )

        coVerify(
            exactly = 0
        ) { getRunWithPathByRunIdUseCase(any()) }

    }

    @Test
    fun `state shows error when run is not found in repo`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase("run-123") } returns null

        val viewModel = buildViewModel(runId = "run-123")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals(
            false,
            state.load
        )
        assertEquals(
            "",
            state.error
        )
        assertNull(
            state.data
        )

    }

    @Test
    fun `RunDeleted event is emitted after successful delete`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase("run-123") } returns fakeRunWithPath
        coEvery { deleteRunUseCase(fakeRun) } returns Unit

        val viewModel = buildViewModel(runId = "run-123")
        advanceUntilIdle()

        viewModel.events.test {

            viewModel.onAction(RunStatsAction.OnDeleteRunClick)
            advanceUntilIdle()

            val event = awaitItem()

            assertTrue(
                event is RunStatsEvent.RunDeleted
            )

            cancelAndIgnoreRemainingEvents()

        }

        coVerify(
            exactly = 1
        ) { deleteRunUseCase(fakeRun) }

    }

    @Test
    fun `Error event is emitted when deleteRun throws`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase("run-123") } returns fakeRunWithPath
        coEvery { deleteRunUseCase(any()) } throws RuntimeException("Network failure")

        val viewModel = buildViewModel(runId = "run-123")
        advanceUntilIdle()

        viewModel.events.test {

            viewModel.onAction(RunStatsAction.OnDeleteRunClick)
            advanceUntilIdle()

            val event = awaitItem()

            assertTrue(
                event is RunStatsEvent.Error
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun `deleteRun does nothing when state data is null`() = runTest {

        coEvery { getRunWithPathByRunIdUseCase("run-123") } returns null

        val viewModel = buildViewModel(runId = "run-123")
        advanceUntilIdle()

        viewModel.onAction(RunStatsAction.OnDeleteRunClick)
        advanceUntilIdle()

        coVerify(
            exactly = 0
        ) { deleteRunUseCase(any()) }

    }

}