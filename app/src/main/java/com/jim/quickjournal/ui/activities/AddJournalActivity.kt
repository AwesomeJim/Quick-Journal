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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jim.quickjournal.R
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Allows user to make a new Journal Entry
 *
 */
@AndroidEntryPoint
class AddJournalActivity : AppCompatActivity(), View.OnClickListener {
    // Fields for views
    private val viewModel: JournalViewModel by viewModels()
    var mJournalTitleEditText: TextInputEditText? = null
    var mJournalBodyEditText: TextInputEditText? = null
    var mDateView: TextView? = null
    lateinit var btn_Cancel: MaterialButton
    lateinit var btn_Save: MaterialButton
    private var mJournalId = DEFAULT_JOURNAL_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal)
        initViews()


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID)
        }
        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            btn_Save.setText(R.string.update_button)
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID)
                /**
                 * Load the Journal entry form the ViewModel
                 */
                viewModel.loadJournalById(mJournalId)
                viewModel.journalItem.observe(this, object : Observer<JournalEntry?> {
                    override fun onChanged(jEntry: JournalEntry?) {
                        viewModel.journalItem.removeObserver(this)
                        populateUI(jEntry)
                    }
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_addjournal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.bar_cancel_btn -> finish()
            R.id.bar_save_btn -> onSaveButtonClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private fun initViews() {
        mJournalTitleEditText = findViewById(R.id.editText_journal_title)
        mJournalBodyEditText = findViewById(R.id.editText_journal_body)
        mDateView = findViewById(R.id.textView_date)
        btn_Cancel = findViewById(R.id.button_cancel)
        btn_Save = findViewById(R.id.button_save)
        btn_Save.setOnClickListener(this)
        btn_Cancel.setOnClickListener(this)
    }

    //Handle the OnclickListeners of the views
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_cancel -> finish()
            R.id.button_save -> onSaveButtonClicked()
            else -> {}
        }
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new Journal Entry data into the underlying database.
     */
    private fun onSaveButtonClicked() {
        val title = mJournalTitleEditText!!.text.toString()
        val body = mJournalBodyEditText!!.text.toString()
        val date = Date()
        val journalEntry = JournalEntry(title = title, body = body, updatedOn = date)

        // insert the task only if mJournalId matches DEFAULT_JOURNAL_ID
        // Otherwise update it
        // call finish in any case
        if (mJournalId == DEFAULT_JOURNAL_ID) {
            // insert new task
            viewModel.insertJournal(journalEntry)
        } else {
            //update task
            journalEntry.id = mJournalId
            viewModel.updateJournal(journalEntry)
        }
        finish()

    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journalEntry the Journal Entry to populate the UI
     */
    private fun populateUI(journalEntry: JournalEntry?) {
        mJournalTitleEditText!!.setText(journalEntry!!.title)
        mJournalBodyEditText!!.setText(journalEntry.body)
    }

    companion object {
        // Extra for the task ID to be received in the intent
        const val EXTRA_JOURNAL_ID = "extraJournalId"

        //Extra for the task ID to be received after rotation
        const val INSTANCE_JOURNAL_ID = "instanceJournalId"

        //Constant for default task id to be used when not in update mode
        private const val DEFAULT_JOURNAL_ID = -1
    }
}