package com.jim.quickjournal

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class QuickJournalApp : MultiDexApplication() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}

