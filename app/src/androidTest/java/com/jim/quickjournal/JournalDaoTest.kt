package com.jim.quickjournal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.jim.quickjournal.db.JournalDatabase
import com.jim.quickjournal.db.dao.JournalDao
import com.jim.quickjournal.db.entity.JournalEntry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@SmallTest
class JournalDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: JournalDatabase
    private lateinit var jounalDao: JournalDao

    @Before
    fun setup() {
        hiltRule.inject()
        jounalDao = database.journalDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertUser() = runBlockingTest {
        val journal = JournalEntry(
            id = 1,
            title = "Testing",
            body = "Testing with Hilt",
            updatedOn = Date()
        )
        jounalDao.insertJournal(journal)
        // When the repository emits a value
        val actual = jounalDao.loadAllJournals()?.first() // Returns the first item in the flow

        assertThat(journal).isIn(actual)


    }
}