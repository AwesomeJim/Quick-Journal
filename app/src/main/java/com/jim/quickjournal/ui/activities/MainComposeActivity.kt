package com.jim.quickjournal.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jim.quickjournal.ui.compose.components.QuickJournalTopAppBar
import com.jim.quickjournal.ui.compose.navigation.AppNavItem
import com.jim.quickjournal.ui.compose.screens.HomeScreen
import com.jim.quickjournal.ui.compose.screens.JournalDetailScreen
import com.jim.quickjournal.ui.compose.theme.QuickJournalTheme
import com.jim.quickjournal.ui.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint

data class AppBarState(
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null
)


@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val journalViewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickJournalTheme {
                navController = rememberNavController()
                val canNavigateBackState = rememberSaveable { (mutableStateOf(false)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                var appBarState by remember { mutableStateOf(AppBarState()) }

                when (navBackStackEntry?.destination?.route) {
                    AppNavItem.Home.screenRoute -> {
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                    }

                    AppNavItem.ViewJournal.routeWithArgs -> {
                        if (!canNavigateBackState.value) canNavigateBackState.value = true
                    }

                    AppNavItem.EditJournal.routeWithArgs -> {
                        if (!canNavigateBackState.value) canNavigateBackState.value = true
                    }
                }
                /*

            if (cancelButtonState) {
                IconButton(onClick = onCancelButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Cancel Editing Journal"
                    )
                }
            }
            if (saveButtonState) {
                IconButton(onClick = onSaveButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Save Journal"
                    )
                }
            }
                 */
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        QuickJournalTopAppBar(
                            title = appBarState.title,
                            modifier = Modifier,
                            navigateUp = { navController.navigateUp() },
                            canNavigateBack = canNavigateBackState.value,
                            actions = {
                                appBarState.actions?.invoke(this)
                            }
                        )
                    }
                ) { paddingValues ->
                    Surface(
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        NavHost(
                            navController, startDestination = AppNavItem.Home.screenRoute,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(AppNavItem.Home.screenRoute) {
                                HomeScreen(
                                    onComposing = {
                                        appBarState = it
                                    },
                                    journalViewModel = journalViewModel,
                                    onJournalEntryClicked = {},
                                    onAddJournalClicked = {}
                                )
                            }

                            composable(AppNavItem.ViewJournal.routeWithArgs,
                                arguments = AppNavItem.ViewJournal.arguments,
                                enterTransition = {
                                    slideInVertically(
                                        animationSpec = tween(700),
                                        initialOffsetY = { it }
                                    )
                                },
                                exitTransition = {
                                    slideOutVertically(
                                        animationSpec = tween(700),
                                        targetOffsetY = { it }
                                    )
                                }
                            ) { navBackStackEntry ->
                                // Retrieve the passed argument
                                val journalIdTypeArg =
                                    navBackStackEntry.arguments?.getInt(AppNavItem.ViewJournal.journalIdTypeArg)
                                JournalDetailScreen(onComposing = {

                                },
                                    journalId = journalIdTypeArg!!,
                                    journalViewModel = journalViewModel,
                                    onEditJournalEntryClicked = {

                                    },
                                    onDeleteJournalClicked = {
                                        journalViewModel.deleteJournal(it)
                                        navController.navigateUp()
                                    })
                            }

                            composable(AppNavItem.EditJournal.routeWithArgs,
                                arguments = AppNavItem.EditJournal.arguments,
                                enterTransition = {
                                    slideInVertically(
                                        animationSpec = tween(700),
                                        initialOffsetY = { it }
                                    )
                                },
                                exitTransition = {
                                    slideOutVertically(
                                        animationSpec = tween(700),
                                        targetOffsetY = { it }
                                    )
                                }
                            ) { navBackStackEntry ->
                                // Retrieve the passed argument
                                val journalIdTypeArg =
                                    navBackStackEntry.arguments?.getInt(AppNavItem.ViewJournal.journalIdTypeArg)

                            }
                        }
                    }
                }
            }
        }
    }

}