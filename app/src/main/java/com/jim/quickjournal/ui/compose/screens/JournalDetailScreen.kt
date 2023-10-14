package com.jim.quickjournal.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        journalViewModel.loadJournalByIdStateView(journalId).collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = "My Journal",
                actions = {
                    IconButton(onClick = {
                        savedJournalUiState.journalEntry?.let {
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
                value = savedJournalUiState.title ?: "",
                onValueChange = {

                },
                Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Title...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = outlinedTextFieldColors,
                enabled = false,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = savedJournalUiState.description ?: "",
                onValueChange = {

                },
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
                enabled = false,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        savedJournalUiState.journalEntry?.let {
                            onDeleteJournalClicked(it)
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
                Button(
                    onClick = {
                        savedJournalUiState.journalEntry?.let {
                            onEditJournalEntryClicked(it)
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                ) {
                    Text("Update")
                }
            }
        }
    }

}