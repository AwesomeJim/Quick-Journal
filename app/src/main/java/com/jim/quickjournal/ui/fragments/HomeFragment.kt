package com.jim.quickjournal.ui.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jim.quickjournal.R
import com.jim.quickjournal.adaptor.JournalAdapter
import com.jim.quickjournal.databinding.FragmentHomeBinding
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, JournalViewModel>(FragmentHomeBinding::inflate) {

    override val viewModel by viewModels<JournalViewModel>()

    //Activity Member Variables
    @Inject
    lateinit var mAdapter: JournalAdapter

    override fun setupUI() {
        super.setupUI()
        // Set the RecyclerView to its corresponding view
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize the adapter and attach it to the RecyclerView
        binding.recyclerView.adapter = mAdapter
    }

    override fun setupVM() {
        super.setupVM()
        lifecycle.coroutineScope.launch {
            viewModel.loadAllJournals()?.collect {
                mAdapter.setJournalsList(
                    context = requireContext(),
                    it
                )
                mAdapter.notifyDataSetChanged()
            }
        }
        mAdapter.setOnItemClickListener { _, any2, _ ->
            val mJournalEntry = any2 as JournalEntry
            val args = Bundle()
            args.putInt(JournalDetailFragment.EXTRA_JOURNAL_ID, mJournalEntry.id)
            viewModel.actionNavigateToDirection(R.id.action_nav_to_journalDetailFragment, args)
        }
    }

}