package com.jim.quickjournal.db

import com.jim.quickjournal.db.dao.JournalDao
import com.jim.quickjournal.db.entity.JournalEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(private val journalDao: JournalDao) {

    fun loadAllJournals(): Flow<List<JournalEntry>?> {
        return journalDao.loadAllJournals()
    }

    fun loadAllJournalWithID(id: Int): Flow<JournalEntry?> =
        journalDao.loadJournalById(id).flowOn(Dispatchers.IO)


    suspend fun deleteJournal(journalEntry: JournalEntry) = withContext(Dispatchers.IO) {
        journalDao.deleteJournal(journalEntry)
    }

    suspend fun insertJournal(journalEntry: JournalEntry) = withContext(Dispatchers.IO) {
        journalDao.insertJournal(journalEntry)
    }

    suspend fun updateJournal(journalEntry: JournalEntry) = withContext(Dispatchers.IO) {
        journalDao.updateJournal(journalEntry)
    }
}