package com.jim.quickjournal.ui.fragments

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jim.quickjournal.R
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


abstract class BaseViewModel(private val journalRepo: JournalRepositoryImpl) :
    ViewModel() {
    suspend fun loadAllJournals(): Flow<List<JournalEntry>?> = journalRepo.loadAllJournals()


    private fun getNavOptions(
        optionalPopUpToId: Int? = null,
        inclusive: Boolean? = null
    ) = NavOptions.Builder().apply {
        setEnterAnim(R.anim.slide_in)
        setExitAnim(R.anim.slide_out)
        setPopExitAnim(R.anim.slide_out)
        optionalPopUpToId?.let {
            setPopUpTo(optionalPopUpToId, inclusive ?: false)
        }
    }.build()


    val finishRequest: LiveData<Unit> get() = _finishRequest
    private val _finishRequest = SingleLiveEvent<Unit>()

    val navigationEvent: LiveData<NavController.() -> Any> get() = _navigationEvent
    private val _navigationEvent = SingleLiveEvent<NavController.() -> Any>()

    private fun navigateInDirection(resId: Int, args: Bundle?) {
        val msg = "^^^^^^^^^^=navigateInDirection=^^^^^^^^^^^^ $resId"
        Timber.e(msg)
        _navigationEvent.postValue {
            navigate(resId, args, getNavOptions())
        }
    }

    fun actionNavigateToDirection(resId: Int, args: Bundle?) = navigateInDirection(
        resId, args
    )

    fun goBackUpTo(
        destinyId: Int,
        inclusive: Boolean
    ) = _navigationEvent.postValue {
        navigate(
            destinyId, null, getNavOptions(
                optionalPopUpToId = destinyId,
                inclusive = inclusive
            )
        )
    }

    protected fun navigateUp() {
        _navigationEvent.postValue {
            navigateUp()
        }
    }
}