package com.jim.quickjournal.db

import com.jim.quickjournal.db.dao.JournalDao
import com.jim.quickjournal.db.entity.JournalEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(private val journalDao: JournalDao) {

    suspend fun loadAllJournals(): List<JournalEntry> = withContext(Dispatchers.IO)  {
        return@withContext journalDao.loadAllJournals()
    }

    suspend fun loadAllJournalWithID(id: Int): JournalEntry = withContext(Dispatchers.IO){
        return@withContext journalDao.loadJournalById(id)
    }

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