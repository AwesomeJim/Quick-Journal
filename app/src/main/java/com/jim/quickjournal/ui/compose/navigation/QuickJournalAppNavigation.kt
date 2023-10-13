package com.jim.quickjournal.ui.compose.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jim.quickjournal.ui.viewmodel.JournalViewModel


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */


/**
 * App nav item
 *
 * @property title - Represent the screen title
 * @property icon - represent the screen icon
 * @property screenRoute - represent the screen route
 * @constructor Create empty App nav item
 */
sealed class AppNavItem(var title: String, private var icon: ImageVector, var screenRoute: String) {

    object Home : AppNavItem("Quick Journal", Icons.Filled.Home, "home")
    object ViewJournal : AppNavItem("View Journal", Icons.Filled.EditNote, "view_journal") {
        const val journalIdTypeArg = "journal_id"
        val routeWithArgs =
            "$screenRoute/{${journalIdTypeArg}}"
        val arguments = listOf(
            navArgument(journalIdTypeArg) { type = NavType.IntType }
        )
    }

    object EditJournal :
        AppNavItem("Edit Journal", Icons.Filled.EditNote, "edit_journal") {
        const val journalIdTypeArg = "journal_id"
        val routeWithArgs =
            "$screenRoute/{$journalIdTypeArg}"
        val arguments = listOf(
            navArgument(journalIdTypeArg) { type = NavType.IntType }
        )
    }
}

/**
 * Navigation graph
 *
 * @param navController
 */

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainViewModel: JournalViewModel,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController, startDestination = AppNavItem.Home.screenRoute,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(AppNavItem.Home.screenRoute) {

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