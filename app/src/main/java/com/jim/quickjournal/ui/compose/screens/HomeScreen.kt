package com.jim.quickjournal.ui.compose.screens

import androidx.compose.runtime.Composable
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.viewmodel.JournalViewModel


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

@Composable
fun HomeScreen(
    journalViewModel: JournalViewModel,
    onJournalEntryClicked: (JournalEntry) -> Unit = {},
    onAddJournalClicked: () -> Unit = {}
) {

}