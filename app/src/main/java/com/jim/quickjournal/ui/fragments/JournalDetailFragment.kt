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

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityJournalDetailBinding
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class JournalDetailFragment :
    BaseFragment<ActivityJournalDetailBinding, JournalViewModel>(ActivityJournalDetailBinding::inflate) {
    // Date formatter
    override val viewModel by viewModels<JournalViewModel>()

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    lateinit var mJournalEntry: JournalEntry
    var mJournalId: Int = DEFAULT_JOURNAL_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJournalId = it.getInt(AddJournalFragment.EXTRA_JOURNAL_ID)
        }
        setHasOptionsMenu(true)
    }

    override fun setupUI() {
        super.setupUI()
        initViews()
        if (mJournalId != DEFAULT_JOURNAL_ID) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loadJournalById(mJournalId).filterNotNull().collect { journalEntry ->
                        populateUI(journalEntry)
                        mJournalEntry = journalEntry
                    }
                }
            }
        }
    }

    /**
     * Initialize the activity Views
     */
    private fun initViews() {
        binding.buttonUpdate.setOnClickListener {
            upDateJournal()
        }
        binding.buttonDelete.setOnClickListener {
            deleteJournal()
        }
    }

    /**
     * populateUI would be called to populate the UI when in view mode
     *
     * @param journalEntry the Journal Entry to populate the UI
     */
    private fun populateUI(journalEntry: JournalEntry?) {
        journalEntry?.let {
            with(binding) {
                editTextJournalTitle.setText(it.title)
                editTextJournalBody.setText(it.body)
                textViewDate.text = dateFormat.format(it.updatedOn)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_journal_detail_activity, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bar_edit_property -> upDateJournal()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Called when update Journal button is clicked
     */
    private fun upDateJournal() {
        val args = Bundle().apply {
            putInt(EXTRA_JOURNAL_ID, mJournalEntry.id)
        }
        viewModel.actionNavigateToDirection(R.id.action_nav_to_AddJournalFragment, args)
    }

    /**
     * Prompts to Deletes a given journal
     * with a dialog
     */
    private fun deleteJournal() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion!")
            .setMessage("are you sure you want to delete this journal?")
            .setPositiveButton("No Cancel", null)
            .setNegativeButton("yes Delete") { _: DialogInterface?, _: Int ->
                viewModel.deleteJournal(mJournalEntry)
                this.findNavController().popBackStack()
            }
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    companion object {
        // Extra for the task ID to be received in the intent
        const val EXTRA_JOURNAL_ID = "extraJournalId"

        // Constant for date format
        private const val DATE_FORMAT = "EEE, d MMM yyyy HH:mm aa"
        private const val DEFAULT_JOURNAL_ID = -1
    }
}