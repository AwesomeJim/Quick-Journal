package com.jim.quickjournal.data.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jim.quickjournal.data.JournalEntry;

import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journals ORDER BY id")
    List<JournalEntry> loadAllJournals();

    @Insert
    void insertJournal(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(JournalEntry journalEntry);

    @Delete
    void deleteJournal(JournalEntry journalEntry);

    @Query("SELECT * FROM journals WHERE id = :id")
    JournalEntry loadJournalById(int id);
}
