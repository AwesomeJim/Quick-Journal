package com.jim.quickjournal.utils

import com.jim.quickjournal.db.entity.JournalEntry
import java.util.Date


/**
 * Created by Awesome Jim on.
 * 13/10/2023
 */

object FakeDataSource {

    val journalEntrySample = JournalEntry(
        id = 8733,
        title = "Magic",
        body = "The Magic you are looking for is in the work you are avoiding ",
        updatedOn = Date()
    )

    val journalEntryListSample = listOf(
        JournalEntry(
            id = 8733,
            title = "Magic",
            body = "The Magic you are looking for is in the work you are avoiding ",
            updatedOn = Date()
        ),
        JournalEntry(
            id = 87331,
            title = "Magic 1 ",
            body = "The Magic you are looking for is in the work you are avoiding ",
            updatedOn = Date()
        ),
        JournalEntry(
            id = 87332,
            title = "Magic 2",
            body = "The Magic you are looking for is in the work you are avoiding ",
            updatedOn = Date()
        ),
        JournalEntry(
            id = 87333,
            title = "Magic 3",
            body = "The Magic you are looking for is in the work you are avoiding ",
            updatedOn = Date()
        )
    )
}