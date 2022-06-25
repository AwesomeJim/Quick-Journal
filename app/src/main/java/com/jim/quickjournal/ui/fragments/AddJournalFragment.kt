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
package com.jim.quickjournal.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityAddJournalBinding
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

/**
 * Allows user to make a new Journal Entry
 *
 */
@AndroidEntryPoint
class AddJournalFragment :
    BaseFragment<ActivityAddJournalBinding, JournalViewModel>(ActivityAddJournalBinding::inflate),
    View.OnClickListener {
    // Fields for views
    override val viewModel by viewModels<JournalViewModel>()

    private var mJournalId = DEFAULT_JOURNAL_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJournalId = it.getInt(EXTRA_JOURNAL_ID)
        }
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID)
        }
        if (mJournalId != DEFAULT_JOURNAL_ID) {
            binding.buttonSave.setText(R.string.update_button)
            /**
             * Load the Journal entry form the ViewModel
             */
            lifecycle.coroutineScope.launch {
                viewModel.loadJournalById(mJournalId)?.collect {
                    populateUI(it)

                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_addjournal, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.bar_cancel_btn -> this.findNavController().popBackStack()
            R.id.bar_save_btn -> onSaveButtonClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private fun initViews() {
        binding.buttonSave.setOnClickListener(this)
        binding.buttonCancel.setOnClickListener(this)
    }

    //Handle the OnclickListeners of the views
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_cancel -> this.findNavController().popBackStack()
            R.id.button_save -> onSaveButtonClicked()
            else -> {}
        }
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new Journal Entry data into the underlying database.
     */
    private fun onSaveButtonClicked() {
        val title = binding.editTextJournalTitle.text.toString()
        val body = binding.editTextBodyLayout.editText?.text.toString()
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
        this.findNavController().popBackStack()

    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journalEntry the Journal Entry to populate the UI
     */
    private fun populateUI(journalEntry: JournalEntry?) {
        journalEntry?.let {
            binding.editTextJournalTitle.setText(it.title)
            binding.editTextBodyLayout.editText?.setText(it.body)
        }
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