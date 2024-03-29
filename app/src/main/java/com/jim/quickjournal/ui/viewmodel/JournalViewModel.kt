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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(private val journalRepo: JournalRepositoryImpl) :
    ViewModel() {

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


    fun deleteJournal(journalEntry: JournalEntry) =
        viewModelScope.launch {
            journalRepo.deleteJournal(journalEntry)

        }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}


/**
 * Saved data Ui State for HomeScreen
 */
data class SavedJournalListUiState(val itemList: List<JournalEntry> = listOf())

