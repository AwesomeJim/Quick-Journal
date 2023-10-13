package com.jim.quickjournal.ui.compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument


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
