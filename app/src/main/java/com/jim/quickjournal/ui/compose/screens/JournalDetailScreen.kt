package com.jim.quickjournal.ui.compose.screens

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.activities.AppBarState
import com.jim.quickjournal.ui.compose.theme.QuickJournalTheme
import timber.log.Timber
import java.util.Date


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

@Composable
fun JournalDetailScreen(
    onComposing: (AppBarState) -> Unit,
    journalId: Int,
    journalViewModel: JournalDetailViewModel,
    onEditJournalEntryClicked: () -> Unit = {},
    onDeleteJournalClicked: (JournalEntry) -> Unit = {}
) {
    journalViewModel.loadJournalByIdState(journalId)
    val savedJournalUiState =
        journalViewModel.savedJournalUiState.collectAsStateWithLifecycle().value

    val showDeleteDialog = rememberSaveable { mutableStateOf(false) }
    Timber.e("saved Journal title: " + savedJournalUiState.journalEntry?.title)
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = "My Journal",
                actions = {
                    IconButton(
                        onClick = onEditJournalEntryClicked
                    ) {
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
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                value = savedJournalUiState.journalEntry?.title ?: "",
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
                value = savedJournalUiState.journalEntry?.body ?: "",
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
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Updated on:",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = dateFormat.format(savedJournalUiState.journalEntry?.updatedOn ?: Date()),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        showDeleteDialog.value = true
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Delete")
                }
                Button(
                    onClick = onEditJournalEntryClicked,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(150.dp),
                ) {
                    Text("Update")
                }
            }
        }
    }
    if (showDeleteDialog.value) {
        AlertDialogError(
            onDismissRequest = {
                showDeleteDialog.value = false
            },
            onConfirmation = {
                showDeleteDialog.value = false
                savedJournalUiState.journalEntry?.let {
                    onDeleteJournalClicked(it)
                }
            },
            dialogTitle = "Delete Journal",
            dialogText = "are you sure, you want to delete this Journal?"
        )
    }

}

@Composable
fun AlertDialogError(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.Dangerous, contentDescription = dialogTitle)
        },
        title = {
            Text(text = dialogTitle)
        },
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        text = {
            Text(text = dialogText)
        },
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun AlertDialogPreview() {
    QuickJournalTheme {
        AlertDialogError(
            onDismissRequest = { },
            onConfirmation = {
                println("Confirmation registered") // Add logic here to handle confirmation.
            },
            dialogTitle = "Alert dialog example",
            dialogText = "This is an example of an alert dialog with buttons."
        )
    }
}