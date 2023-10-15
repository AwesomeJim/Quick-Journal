package com.jim.quickjournal.ui.compose.screens

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jim.quickjournal.adaptor.JournalAdapter
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.activities.AppBarState
import com.jim.quickjournal.ui.compose.components.AddFloatingActionButton
import com.jim.quickjournal.ui.compose.theme.QuickJournalTheme
import com.jim.quickjournal.ui.viewmodel.JournalViewModel
import com.jim.quickjournal.utils.FakeDataSource
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

@Composable
fun HomeScreen(
    onComposing: (AppBarState) -> Unit,
    journalViewModel: JournalViewModel,
    onJournalEntryClicked: (JournalEntry) -> Unit = {},
    onAddJournalClicked: () -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = "Quick Journal",
                actions = {
                    IconButton(onClick = onAddJournalClicked) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = null
                        )
                    }
                }
            )
        )
    }

    val savedJournalListUiState = journalViewModel
        .savedJournalListUiState
        .collectAsStateWithLifecycle().value
    Timber.e("savedJournalListUiState : " + savedJournalListUiState.itemList.size)
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
                JournalListItem(journalEntry = journal,
                    onClick = {
                        onJournalEntryClicked(journal)
                    }
                )
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
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Start Journaling, and save your best moments and notes",
                        color = MaterialTheme.colorScheme.onSurface
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
        AddFloatingActionButton(
            extended = lazyListState.isScrollingUp(),
            onClick = onAddJournalClicked
        )
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

val dateFormat = SimpleDateFormat(JournalAdapter.DATE_FORMAT, Locale.getDefault())
private val dateFormatInit = SimpleDateFormat(JournalAdapter.DATE_FORMAT_INIT, Locale.getDefault())

@Composable
fun JournalListItem(
    journalEntry: JournalEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .animateContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 4.dp, horizontal = 4.dp)
                .clickable {
                    onClick()
                }
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .aspectRatio(1f)
                    .background(Color(JournalAdapter.randomColor), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dateFormatInit.format(journalEntry.updatedOn),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier.padding(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 0.dp, horizontal = 0.dp)
                ) {
                    Text(
                        text = journalEntry.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    MenuItemButton(
                        onClick = onClick,
                        modifier = modifier
                    )
                }
                Text(
                    text = journalEntry.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Start
                )
                Divider(
                    modifier = modifier.padding(horizontal = 2.dp, vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row {
                    Text(
                        text = "Updated on:",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = dateFormat.format(journalEntry.updatedOn),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun JournalListItemPreView() {
    QuickJournalTheme {
        JournalListItem(
            journalEntry = FakeDataSource.journalEntrySample,
            onClick = {}
        )
    }

}