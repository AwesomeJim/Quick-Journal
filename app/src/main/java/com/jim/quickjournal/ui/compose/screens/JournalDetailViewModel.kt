package com.jim.quickjournal.ui.compose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 15/10/2023
 */

@HiltViewModel
class JournalDetailViewModel @Inject constructor(
    private val journalRepo: JournalRepositoryImpl
) : ViewModel() {

    // Search UI state
    private val _uiState = MutableStateFlow(JournalDetailsUiState())

    // Backing property to avoid state updates from other classes
    val savedJournalUiState: StateFlow<JournalDetailsUiState> = _uiState.asStateFlow()


    fun loadJournalByIdState(id: Int) {
        viewModelScope.launch {
            journalRepo.loadAllJournalWithID(id).catch {
                Timber.tag("Error").e("while fetching note id %s", id)
            }.first().also { fetchedJournal ->
                if (fetchedJournal != null) {
                    _uiState.update { currentState ->
                        val journalEntry = JournalEntry(
                            id = fetchedJournal.id,
                            title = fetchedJournal.title,
                            body = fetchedJournal.body,
                            updatedOn = fetchedJournal.updatedOn
                        )
                        currentState.copy(
                            journalEntry = journalEntry
                        )
                    }
                }
            }
        }
    }
}


data class JournalDetailsUiState(val journalEntry: JournalEntry? = null)