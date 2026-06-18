package com.rk.pace.data.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rk.pace.data.room.PaceDatabase
import com.rk.pace.data.room.entity.DeleteRunEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteRunDaoTest {

    private lateinit var database: PaceDatabase
    private lateinit var deleteRunDao: DeleteRunDao

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PaceDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        deleteRunDao = database.deleteRunDao()

    }

    @After
    fun down() {
        database.close()
    }

    @Test
    fun insertDeleteRun_and_getAllDeleteRuns_returns_inserted_entry() = runTest {

        val entry = DeleteRunEntity(
            runId = "run-001",
            userId = "user-001"
        )

        deleteRunDao.insertDeleteRun(entry)

        val result = deleteRunDao.getAllDeleteRuns()

        assertEquals(
            1,
            result.size
        )
        assertEquals(
            entry,
            result.first()
        )

    }

    @Test
    fun getAllDeleteRuns_returns_empty_when_nothing_inserted() = runTest {

        val result = deleteRunDao.getAllDeleteRuns()

        assertTrue(
            result.isEmpty()
        )

    }

    @Test
    fun removeDeleteRun_removes_entry_after_firebase_deletion_confirmed() = runTest {

        val entry = DeleteRunEntity(
            runId = "run-001",
            userId = "user-001"
        )

        deleteRunDao.insertDeleteRun(entry)
        deleteRunDao.removeDeleteRun(entry)

        val result = deleteRunDao.getAllDeleteRuns()

        assertTrue(
            result.isEmpty()
        )

    }

    @Test
    fun getAllDeleteRuns_returns_multiple_pending_deletions() = runTest {

        val entry1 = DeleteRunEntity(
            runId = "run-001",
            userId = "user-001"
        )
        val entry2 = DeleteRunEntity(
            runId = "run-002",
            userId = "user-001"
        )
        val entry3 = DeleteRunEntity(
            runId = "run-003",
            userId = "user-001"
        )

        deleteRunDao.insertDeleteRun(entry1)
        deleteRunDao.insertDeleteRun(entry2)
        deleteRunDao.insertDeleteRun(entry3)

        val result = deleteRunDao.getAllDeleteRuns()

        assertEquals(
            3,
            result.size
        )

    }

    @Test
    fun insertDeleteRun_with_REPLACE_does_not_create_duplicate() = runTest {

        val entry = DeleteRunEntity(
            runId = "run-001",
            userId = "user-001"
        )

        deleteRunDao.insertDeleteRun(entry)
        deleteRunDao.insertDeleteRun(entry)

        val result = deleteRunDao.getAllDeleteRuns()

        assertEquals(
            1,
            result.size
        )

    }

    @Test
    fun deleteRun_entry_persists_even_when_no_matching_run_exists_in_runs_table() = runTest {

        val entry = DeleteRunEntity(
            runId = "orphan-run-999",
            userId = "user-001"
        )

        deleteRunDao.insertDeleteRun(entry)

        val result = deleteRunDao.getAllDeleteRuns()

        assertEquals(
            1,
            result.size
        )
        assertEquals(
            "orphan-run-999",
            result.first().runId
        )

    }

}