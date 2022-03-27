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
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.jim.quickjournal.R
import com.jim.quickjournal.databinding.ActivityJournalDetailBinding
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class JournalDetailFragment : Fragment() {
    // Date formatter
    private val viewModel: JournalViewModel by viewModels()

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    private var _binding: ActivityJournalDetailBinding? = null
    private val binding get() = _binding!!
    internal var view: View? = null

    lateinit var mJournalEntry: JournalEntry
    private val DEFAULT_JOURNAL_ID = -1
    var mJournalId: Int = DEFAULT_JOURNAL_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJournalId = it.getInt(AddJournalFragment.EXTRA_JOURNAL_ID)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityJournalDetailBinding.inflate(inflater, container, false)
        view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        initViews()
        if (mJournalId != DEFAULT_JOURNAL_ID) {
            lifecycle.coroutineScope.launch {
                viewModel.loadJournalById(mJournalId).collect {
                    //journalEntry.removeObserver(this);
                    populateUI(it)
                    mJournalEntry = it
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
    private fun populateUI(journalEntry: JournalEntry) {
        binding.editTextJournalTitle.setText(journalEntry.title)
        binding.editTextJournalBody.setText(journalEntry.body)
        binding.textViewDate.text = dateFormat.format(journalEntry.updatedOn)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_journal_detail_activity, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        val args = Bundle()
        args.putInt(EXTRA_JOURNAL_ID, mJournalEntry.id)
        this.findNavController().navigate(R.id.action_nav_to_AddJournalFragment, args)
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
    }
}