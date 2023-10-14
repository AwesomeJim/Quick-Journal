package com.jim.quickjournal.ui.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.ui.activities.AppBarState


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalScreen(
    onComposing: (AppBarState) -> Unit,
    journalId: Int?,
    journalViewModel: AddJournalViewModel,
    onSaveJournalEntryClicked: () -> Unit = {},
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
                    IconButton(
                        onClick = onSaveJournalEntryClicked
                    ) {
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
    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = journalViewModel.title,
                onValueChange = journalViewModel::onTitleChanged,
                Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Title...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = journalViewModel.description,
                onValueChange = journalViewModel::onDescriptionChanged,
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                placeholder = {
                    Text(
                        text = "Description...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = outlinedTextFieldColors,
            )
        }
    }

}