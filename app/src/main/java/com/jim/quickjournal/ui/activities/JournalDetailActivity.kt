/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jim.quickjournal.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jim.quickjournal.R
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class JournalDetailActivity : AppCompatActivity() {
    // Date formatter
    private val viewModel: JournalViewModel by viewModels()

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    var mJournalTitleEditText: TextInputEditText? = null
    var mJournalBodyEditText: TextInputEditText? = null
    var mDateView: TextView? = null
    lateinit var btn_delete: MaterialButton
    lateinit var btn_update: MaterialButton
    lateinit var mJournalEntry: JournalEntry
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_detail)
        initViews()
        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            val mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, -1)
            viewModel.loadJournalById(mJournalId)
            viewModel.journalItem.observe(this) { jEntry: JournalEntry ->
                //journalEntry.removeObserver(this);
                populateUI(jEntry)
                mJournalEntry = jEntry
            }
        }
    }

    /**
     * Initialize the activity Views
     */
    private fun initViews() {
        mJournalTitleEditText = findViewById(R.id.editText_journal_title)
        mJournalBodyEditText = findViewById(R.id.editText_journal_body)
        mDateView = findViewById(R.id.textView_date)
        btn_delete = findViewById(R.id.button_delete)
        btn_update = findViewById(R.id.button_update)
        btn_update.setOnClickListener {
            upDateJournal()
        }
        btn_delete.setOnClickListener {
            deleteJournal()
        }
    }

    /**
     * populateUI would be called to populate the UI when in view mode
     *
     * @param journalEntry the Journal Entry to populate the UI
     */
    private fun populateUI(journalEntry: JournalEntry) {
        mJournalTitleEditText!!.setText(journalEntry.title)
        mJournalBodyEditText!!.setText(journalEntry.body)
        mDateView!!.text = dateFormat.format(journalEntry.updatedOn)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_journal_detail_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        when (id) {
            R.id.bar_edit_property -> upDateJournal()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Called when update Journal button is clicked
     */
    private fun upDateJournal() {
        val intent = Intent(this@JournalDetailActivity, AddJournalActivity::class.java)
        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, mJournalEntry.id)
        startActivity(intent)
    }

    /**
     * Prompts to Deletes a given journal
     * with a dialog
     */
    private fun deleteJournal() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion!")
            .setMessage("are you sure you want to delete this journal?")
            .setPositiveButton("No Cancel", null)
            .setNegativeButton("yes Delete") { _: DialogInterface?, _: Int ->
                viewModel.deleteJournal(mJournalEntry)
            }
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    companion object {
        // Extra for the task ID to be received in the intent
        const val EXTRA_JOURNAL_ID = "extraJournalId"

        // Constant for date format
        private const val DATE_FORMAT = "EEE, d MMM yyyy HH:mm aa"
    }
}