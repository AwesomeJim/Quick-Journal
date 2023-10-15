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
import java.util.Date
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 14/10/2023
 */

@HiltViewModel
class AddJournalViewModel @Inject constructor(
    private val journalRepo: JournalRepositoryImpl
) : ViewModel() {

    // Search UI state
    private val _uiState = MutableStateFlow(SavedJournalUiState())

    // Backing property to avoid state updates from other classes
    val savedJournalUiState: StateFlow<SavedJournalUiState> = _uiState.asStateFlow()


    fun onTitleChanged(newtTitle: String) {
        Timber.tag("onTitleChanged").e("title %s", newtTitle)
        _uiState.update { currentState ->
            currentState.copy(
                title = newtTitle
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                description = description
            )
        }
    }

    fun loadJournalByIdState(id: Int) {
        Timber.e("loadJournalByIdState onTitleChanged title: %s", id)
        viewModelScope.launch {
            journalRepo.loadAllJournalWithID(id).catch {
                Timber.tag("Error").e("while fetching note id %s", id)
            }.first().also { fetchedJournal ->
                if (fetchedJournal != null) {
                    onTitleChanged(fetchedJournal.title)
                    onDescriptionChanged(fetchedJournal.body)
                    _uiState.update { currentState ->
                        currentState.copy(
                            id = fetchedJournal.id,
                            updatedOn = fetchedJournal.updatedOn,
                            isEditing = true
                        )
                    }
                }
            }
        }
    }


    fun savedJournal() {
        val title = _uiState.value.title ?: ""
        val description = _uiState.value.description ?: ""
        val date = Date()
        var journalEntry = JournalEntry(title = title, body = description, updatedOn = date)
        viewModelScope.launch {
            if (_uiState.value.isEditing) {
                journalEntry = JournalEntry(
                    id = _uiState.value.id!!,
                    title = title,
                    body = description,
                    updatedOn = date
                )
                journalRepo.updateJournal(journalEntry)
            } else {
                journalRepo.insertJournal(journalEntry)
            }
        }

    }
}

/**
 * Saved data Ui State for HomeScreen
 */
data class SavedJournalUiState(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val journalEntry: JournalEntry? = null,
    val updatedOn: Date? = null,
    val isEditing: Boolean = false,
    val canBeSaved: Boolean = false,
    val isTitleValid: Boolean = false,
    val isDescriptionValid: Boolean = false
)