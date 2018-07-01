package com.jim.quickjournal.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.jim.quickjournal.db.converter.DateConverter;
import com.jim.quickjournal.db.dao.JournalDao;
import com.jim.quickjournal.db.entity.JournalEntry;

@Database(entities = {JournalEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class JournalDatabase extends RoomDatabase {

    private static final String LOG_TAG = JournalDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "quickjournal";
    private static JournalDatabase sInstance;

    public static JournalDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        JournalDatabase.class, JournalDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract JournalDao journalDao();

}