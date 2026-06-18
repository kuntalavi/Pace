package com.rk.pace.data.tracker

import app.cash.turbine.test
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracker.LocationTracker
import com.rk.pace.domain.tracker.RunTrackServiceController
import com.rk.pace.domain.tracker.TimeTracker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TrackerManagerImpTest {

    private lateinit var runTrackServiceController: RunTrackServiceController
    private lateinit var locationTracker: LocationTracker
    private lateinit var timeTracker: TimeTracker
    private lateinit var trackerManager: TrackerManagerImp

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {

        runTrackServiceController = mockk(relaxed = true)
        locationTracker = mockk(relaxed = true)
        timeTracker = mockk(relaxed = true)

        trackerManager = TrackerManagerImp(
            scope = TestScope(
                UnconfinedTestDispatcher()
            ),
            runTrackServiceController = runTrackServiceController,
            locationTracker = locationTracker,
            timeTracker = timeTracker
        )

    }

    @Test
    fun `start calls startRunTrackService on the service controller`() {

        trackerManager.start()

        verify { runTrackServiceController.startRunTrackService() }

    }

    @Test
    fun `start calls startTimer on the time tracker`() {

        trackerManager.start()

        verify { timeTracker.startTimer(any()) }

    }

    @Test
    fun `location emits from activeTrackLocation when isAct is true`() = runTest {

        val activePoint = RunPathPoint(
            lat = 1.0,
            long = 1.0
        )
        val passivePoint = RunPathPoint(
            lat = 0.0,
            long = 0.0
        )

        every { locationTracker.activeTrackLocation } returns flowOf(activePoint)
        every { locationTracker.passiveLocation } returns flowOf(passivePoint)

        trackerManager.location.test {

            assertEquals(
                passivePoint,
                awaitItem()
            )

            trackerManager.start()

            val emitted = awaitItem()

            assertEquals(
                activePoint,
                emitted
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

}