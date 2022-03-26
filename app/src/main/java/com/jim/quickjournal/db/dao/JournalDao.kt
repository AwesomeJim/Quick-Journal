/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jim.quickjournal.db.dao

import androidx.room.*
import com.jim.quickjournal.db.entity.JournalEntry

/**
 * Room Data Access Object
 */
@Dao
interface JournalDao {
    @Query("SELECT * FROM journals ORDER BY updated_on DESC")
    fun loadAllJournals(): List<JournalEntry>

    @Insert
    suspend fun insertJournal(journalEntry: JournalEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateJournal(journalEntry: JournalEntry)

    @Delete
    suspend fun deleteJournal(journalEntry: JournalEntry)

    @Query("SELECT * FROM journals WHERE id = :id")
    fun loadJournalById(id: Int): JournalEntry
}