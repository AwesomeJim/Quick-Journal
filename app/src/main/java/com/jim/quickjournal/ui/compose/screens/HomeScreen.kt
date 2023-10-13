package com.jim.quickjournal.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.compose.components.AddFloatingActionButton
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
    val savedJournalListUiState = journalViewModel
        .savedJournalListUiState
        .collectAsStateWithLifecycle().value

    //
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        if (savedJournalListUiState.itemList.isNotEmpty()) {
            itemsIndexed(
                items = savedJournalListUiState.itemList,
                // Provide a unique key based on the item  content, for our case we use locationId
                key = { _, item -> item.id }
            ) { _, journal ->

            }
        } else {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Text(
                        text = "Start Journaling, and save your best moments and notes",
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
    // Overlay the FloatingActionButton
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (savedJournalListUiState.itemList.isNotEmpty()) {
            AddFloatingActionButton(
                extended = lazyListState.isScrollingUp(),
                onClick = onAddJournalClicked
            )
        }
    }
}

/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}