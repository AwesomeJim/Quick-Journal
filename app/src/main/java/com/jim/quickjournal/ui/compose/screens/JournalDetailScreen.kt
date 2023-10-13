package com.jim.quickjournal.ui.compose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.activities.AppBarState
import com.jim.quickjournal.ui.viewmodel.JournalViewModel


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

@Composable
fun JournalDetailScreen(
    onComposing: (AppBarState) -> Unit,
    journalId: Int,
    journalViewModel: JournalViewModel,
    onEditJournalEntryClicked: (JournalEntry) -> Unit = {},
    onDeleteJournalClicked: (JournalEntry) -> Unit = {}
) {
    val savedJournalUiState =
        journalViewModel.loadJournalByIdState(journalId).collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = "My Journal",
                actions = {
                    IconButton(onClick = {
                        savedJournalUiState.item?.let {
                            onEditJournalEntryClicked(it)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Edit Journal"
                        )
                    }
                }
            )
        )
    }

}