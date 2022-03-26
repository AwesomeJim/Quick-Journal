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
package com.jim.quickjournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(private val journalRepo: JournalRepositoryImpl) :
    ViewModel() {

    private val _journalList = MutableLiveData<List<JournalEntry>>()

    val journalList: LiveData<List<JournalEntry>>
        get() = _journalList

    private val _journalItem = MutableLiveData<JournalEntry>()
    val journalItem: LiveData<JournalEntry>
        get() = _journalItem

    fun loadAllJournals() {
        viewModelScope.launch {
            _journalList.value = journalRepo.loadAllJournals()
        }

    }

    fun insertJournal(journalEntry: JournalEntry) {
        viewModelScope.launch {
            journalRepo.insertJournal(journalEntry)
        }
    }

    fun updateJournal(journalEntry: JournalEntry) {
        viewModelScope.launch {
            journalRepo.updateJournal(journalEntry)
        }
    }

    fun deleteJournal(journalEntry: JournalEntry) {
        viewModelScope.launch {
            journalRepo.deleteJournal(journalEntry)
        }
    }


    fun loadJournalById(id: Int) {
        viewModelScope.launch {
            _journalItem.value = journalRepo.loadAllJournalWithID(id)
        }
    }

}