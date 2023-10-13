package com.jim.quickjournal.ui.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jim.quickjournal.R
import com.jim.quickjournal.ui.compose.theme.QuickJournalTheme

/**
 * Created by awesome jim on.
 * 29/09/2023
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickJournalTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false,
    actions: @Composable() (RowScope.() -> Unit)
) {
    TopAppBar(
        title = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                text = title,
                fontSize = 22.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
        actions = actions,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    QuickJournalTheme {
        QuickJournalTopAppBar("Awesome Jim",
            actions = {}
        )
    }
}


/**
 * Shows the floating action button.
 *
 * @param extended Whether the tab should be shown in its expanded state.
 */
@Composable
fun AddFloatingActionButton(
    extended: Boolean,
    onClick: () -> Unit
) {
    // how it should animate.
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a journal",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            // Toggle the visibility of the content with animation.
            AnimatedVisibility(extended) {
                Text(
                    text = "Add a Journal",
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }
}