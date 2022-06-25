package com.jim.quickjournal.di

import android.content.Context
import androidx.room.Room
import com.jim.quickjournal.db.JournalDatabase
import com.jim.quickjournal.db.dao.JournalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): JournalDatabase {
        return Room.databaseBuilder(
            appContext,
            JournalDatabase::class.java,
            "quickjournal"
        ).build()
    }


    @Provides
    @Singleton
    fun provideChannelDao(appDatabase: JournalDatabase): JournalDao {
        return appDatabase.journalDao()
    }
}