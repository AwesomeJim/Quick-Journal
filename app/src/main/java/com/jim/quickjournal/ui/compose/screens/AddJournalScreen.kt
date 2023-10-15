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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.ui.activities.AppBarState


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */


@Composable
fun AddJournalScreen(
    onComposing: (AppBarState) -> Unit,
    journalId: Int?,
    journalViewModel: AddJournalViewModel,
    onSaveJournalEntryClicked: () -> Unit = {},
    onCancelJournalClicked: () -> Unit = {}
) {
    //Load the Journal Id once
    LaunchedEffect(key1 = true) {
        //Load a saved journal if the note id is not empty
        journalId?.let {
            journalViewModel.loadJournalByIdState(it)
        }
    }

    val savedJournalUiState =
        journalViewModel.savedJournalUiState.collectAsStateWithLifecycle().value

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
    JournalDetails(
        savedJournalUiState,
        journalViewModel,
        onSaveJournalEntryClicked = onSaveJournalEntryClicked,
        onCancelJournalClicked = onCancelJournalClicked
    )

}

@Composable
fun JournalDetails(
    savedJournalUiState: SavedJournalUiState,
    journalViewModel: AddJournalViewModel,
    onSaveJournalEntryClicked: () -> Unit = {},
    onCancelJournalClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            JournalEntryTitle(
                title = savedJournalUiState.title ?: "",
                journalViewModel = journalViewModel,
            )
            Spacer(modifier = Modifier.height(10.dp))
            JournalEntryDescription(
                des = savedJournalUiState.description ?: "",
                journalViewModel = journalViewModel,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = onCancelJournalClicked,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onSaveJournalEntryClicked,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun JournalEntryTitle(
    title: String,
    journalViewModel: AddJournalViewModel,
    modifier: Modifier = Modifier,
) {
    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
    OutlinedTextField(
        value = title,
        onValueChange = {
            journalViewModel.onTitleChanged(it)
        },
        modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Title...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        colors = outlinedTextFieldColors
    )

}

@Composable
fun JournalEntryDescription(
    des: String,
    journalViewModel: AddJournalViewModel,
    modifier: Modifier = Modifier
) {
    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
    OutlinedTextField(
        value = des,
        onValueChange = {
            journalViewModel.onDescriptionChanged(it)
        },
        modifier
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