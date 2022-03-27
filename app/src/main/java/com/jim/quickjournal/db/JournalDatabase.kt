package com.jim.quickjournal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jim.quickjournal.db.converter.DateConverter
import com.jim.quickjournal.db.dao.JournalDao
import com.jim.quickjournal.db.entity.JournalEntry

@Database(entities = [JournalEntry::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}