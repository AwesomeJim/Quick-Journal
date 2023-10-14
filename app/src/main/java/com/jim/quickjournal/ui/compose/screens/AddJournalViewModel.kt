package com.jim.quickjournal.ui.compose.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jim.quickjournal.db.JournalRepositoryImpl
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.viewmodel.JournalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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


    var title by mutableStateOf(""); private set
    fun onTitleChanged(title: String) {
        this.title = title
    }

    var description by mutableStateOf(""); private set

    fun onDescriptionChanged(description: String) {
        this.description = description
    }

    fun loadJournalByIdState(id: Int): StateFlow<SavedJournalUiState> =
        journalRepo.loadAllJournalWithID(id).map { fetchedJournal ->
            if (fetchedJournal != null) {
                onTitleChanged(fetchedJournal.title)
                onDescriptionChanged(fetchedJournal.body)
                SavedJournalUiState(
                    title,
                    description,
                    updatedOn = fetchedJournal.updatedOn,
                    isEditing = true
                )
            } else {
                SavedJournalUiState()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(JournalViewModel.TIMEOUT_MILLIS),
            initialValue = SavedJournalUiState()
        )

    fun updateTitle(journalEntry: JournalEntry, isEditing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(

            )
        }
    }

    fun savedJournal() {
        viewModelScope.launch {

        }

    }
}

/**
 * Saved data Ui State for HomeScreen
 */
data class SavedJournalUiState(
    val title: String? = null,
    val description: String? = null,
    val updatedOn: Date? = null,
    val isEditing: Boolean = false,
    val canBeSaved: Boolean = false,
    val isTitleValid: Boolean = false,
    val isDescriptionValid: Boolean = false
)