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
package com.jim.quickjournal.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.views.fragments.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(private val journalRepo: JournalRepositoryImpl) :
    BaseViewModel(journalRepo) {

    fun loadAllJournals(): Flow<List<JournalEntry>?> = journalRepo.loadAllJournals()

    // Search UI state
    private val _uiState = MutableStateFlow(SavedJournalUiState())

    // Backing property to avoid state updates from other classes
    val savedJournalUiState: StateFlow<SavedJournalUiState> = _uiState.asStateFlow()

    /**
     * Saved journal list ui state - used by Compose
     */
    val savedJournalListUiState: StateFlow<SavedJournalListUiState> =
        journalRepo.loadAllJournals().map { list ->
            if (list != null) {
                SavedJournalListUiState(list)
            } else {
                SavedJournalListUiState()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = SavedJournalListUiState()
        )

    fun insertJournal(journalEntry: JournalEntry) =
        viewModelScope.launch {
            journalRepo.insertJournal(journalEntry)

        }

    fun updateJournal(journalEntry: JournalEntry) =
        viewModelScope.launch {
            journalRepo.updateJournal(journalEntry)
        }

    fun deleteJournal(journalEntry: JournalEntry) =
        viewModelScope.launch {
            journalRepo.deleteJournal(journalEntry)

        }

    fun loadJournalById(id: Int): Flow<JournalEntry?> = journalRepo.loadAllJournalWithID(id)

    fun loadJournalByIdState(id: Int): StateFlow<SavedJournalUiState> =
        journalRepo.loadAllJournalWithID(id).map { journal ->
            if (journal != null) {
                SavedJournalUiState(journal)
            } else {
                SavedJournalUiState()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = SavedJournalUiState()
        )

    fun updateTitle(journalEntry: JournalEntry, isEditing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                item = journalEntry,
                isEditing = isEditing
            )
        }
    }

    fun updateSavedJournalUiState(journalEntry: JournalEntry, isEditing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                item = journalEntry,
                isEditing = isEditing
            )
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}


/**
 * Saved data Ui State for HomeScreen
 */
data class SavedJournalListUiState(val itemList: List<JournalEntry> = listOf())

/**
 * Saved data Ui State for HomeScreen
 */
data class SavedJournalUiState(
    val item: JournalEntry? = null,
    val isEditing: Boolean = false,
    val canBeSaved: Boolean = false,
    val isTitleValid: Boolean = false,
    val isBodyValid: Boolean = false
)