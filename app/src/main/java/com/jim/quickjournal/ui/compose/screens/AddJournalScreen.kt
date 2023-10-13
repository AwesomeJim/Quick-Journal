package com.jim.quickjournal.ui.compose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Save
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
fun AddJournalScreen(
    onComposing: (AppBarState) -> Unit,
    journalId: Int?,
    journalViewModel: JournalViewModel,
    onSaveJournalEntryClicked: (JournalEntry) -> Unit = {},
    onCancelJournalClicked: () -> Unit = {}
) {
    val savedJournalUiState =
        journalId?.let {
            journalViewModel.loadJournalByIdState(it).collectAsStateWithLifecycle().value
        }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = "Add Journal",
                actions = {
                    IconButton(onClick = onCancelJournalClicked) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Cancel Editing Journal"
                        )
                    }
                    IconButton(onClick = {
                        savedJournalUiState?.item?.let {
                            onSaveJournalEntryClicked(it)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Save,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Save Journal"
                        )
                    }
                }
            )
        )
    }

}