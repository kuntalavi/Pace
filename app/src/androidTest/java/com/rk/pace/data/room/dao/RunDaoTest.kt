package com.rk.pace.data.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.rk.pace.data.fakePathPoints
import com.rk.pace.data.fakeRun
import com.rk.pace.data.fakeRun2
import com.rk.pace.data.fakeUser
import com.rk.pace.data.room.PaceDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RunDaoTest {

    private lateinit var database: PaceDatabase
    private lateinit var runDao: RunDao

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PaceDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        runDao = database.runDao()

        runTest {
            database.userDao().insertUser(fakeUser)
        }

    }

    @After
    fun down() {
        database.close()
    }

    @Test
    fun insertRun_and_getAllRuns_emits_inserted_run() = runTest {

        runDao.insertRun(fakeRun)

        runDao.getAllRuns().test {

            val runs = awaitItem()

            assertEquals(
                1,
                runs.size
            )
            assertEquals(
                fakeRun,
                runs.first()
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun getAllRuns_returns_runs_ordered_by_timestamp_descending() = runTest {

        runDao.insertRun(fakeRun)
        runDao.insertRun(fakeRun2)

        runDao.getAllRuns().test {

            val runs = awaitItem()

            assertEquals(
                2,
                runs.size
            )
            assertEquals(
                "run-002",
                runs[0].runId
            )
            assertEquals(
                "run-001",
                runs[1].runId
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun insertRun_with_REPLACE_strategy_updates_existing_run() = runTest {

        runDao.insertRun(fakeRun)

        val updatedRun = fakeRun.copy(
            title = "New Title"
        )
        runDao.insertRun(updatedRun)

        runDao.getAllRuns().test {

            val runs = awaitItem()

            assertEquals(
                1,
                runs.size
            )
            assertEquals(
                "New Title",
                runs.first().title
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun deleteRun_removes_run_from_database() = runTest {

        runDao.insertRun(fakeRun)

        runDao.deleteRun(fakeRun)

        runDao.getAllRuns().test {

            val runs = awaitItem()

            assertTrue(
                runs.isEmpty()
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun getRunWithPathByRunId_returns_run_with_its_path_points() = runTest {

        runDao.insertRun(fakeRun)
        runDao.insertRunPathPoints(fakePathPoints)

        val result = runDao.getRunWithPathByRunId("run-001")

        assertNotNull(
            result
        )
        assertEquals(
            fakeRun,
            result!!.run
        )
        assertEquals(
            2,
            result.path.size
        )

        assertTrue(
            result.path.all { it.runId == "run-001" }
        )

    }

    @Test
    fun getRunWithPathByRunId_returns_null_for_nonexistent_runId() = runTest {

        val result = runDao.getRunWithPathByRunId("nonexistent-id")

        assertNull(
            result
        )

    }

    @Test
    fun getRunWithPathByRunId_returns_run_with_empty_path_when_no_points_inserted() = runTest {

        runDao.insertRun(fakeRun)

        val result = runDao.getRunWithPathByRunId("run-001")

        assertNotNull(
            result
        )
        assertEquals(
            fakeRun,
            result!!.run
        )
        assertTrue(
            result.path.isEmpty()
        )

    }

    @Test
    fun deleteRun_cascades_and_removes_associated_path_points() = runTest {

        runDao.insertRun(fakeRun)
        runDao.insertRunPathPoints(fakePathPoints)

        val before = runDao.getRunWithPathByRunId("run-001")

        assertEquals(
            2,
            before!!.path.size
        )

        runDao.deleteRun(fakeRun)

        val after = runDao.getRunWithPathByRunId("run-001")

        assertNull(
            after
        )

    }

    @Test
    fun getUnsyncedRunsWithPath_returns_only_unsynced_runs() = runTest {

        runDao.insertRun(fakeRun)
        runDao.insertRun(fakeRun2)

        val unsynced = runDao.getUnsyncedRunsWithPath()

        assertEquals(
            1,
            unsynced.size
        )
        assertEquals(
            "run-001",
            unsynced.first().run.runId
        )

    }

    @Test
    fun getUnsyncedRunsWithPath_returns_empty_when_all_runs_are_synced() = runTest {

        runDao.insertRun(fakeRun2)

        val unsynced = runDao.getUnsyncedRunsWithPath()

        assertTrue(
            unsynced.isEmpty()
        )

    }

    @Test
    fun markRunAsSynced_updates_synced_flag_to_true() = runTest {

        runDao.insertRun(fakeRun)

        runDao.markRunAsSynced("run-001")

        val unsynced = runDao.getUnsyncedRunsWithPath()
        assertTrue(
            unsynced.isEmpty()
        )

    }

    @Test
    fun saveRestoredRun_inserts_run_and_path_points_atomically() = runTest {

        runDao.saveRestoredRun(fakeRun, fakePathPoints)

        val result = runDao.getRunWithPathByRunId("run-001")

        assertNotNull(
            result
        )
        assertEquals(
            fakeRun,
            result!!.run
        )
        assertEquals(
            2,
            result.path.size
        )

    }

    @Test
    fun getRunsForWeek_returns_only_runs_within_given_timestamp_range() = runTest {

        runDao.insertRun(fakeRun)
        runDao.insertRun(fakeRun2)

        runDao.getRunsForWeek(
            weekStart = 500_000L,
            weekEnd = 1_500_000L
        ).test {

            val runs = awaitItem()

            assertEquals(
                1,
                runs.size
            )
            assertEquals(
                "run-001",
                runs.first().runId
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun getRunsForWeek_returns_empty_when_no_runs_in_range() = runTest {

        runDao.insertRun(fakeRun)

        runDao.getRunsForWeek(
            weekStart = 5_000_000L,
            weekEnd = 9_000_000L
        ).test {

            val runs = awaitItem()

            assertTrue(
                runs.isEmpty()
            )

            cancelAndIgnoreRemainingEvents()

        }

    }

}