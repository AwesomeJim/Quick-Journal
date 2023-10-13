package com.jim.quickjournal.ui.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jim.quickjournal.R
import com.jim.quickjournal.adaptor.JournalAdapter
import com.jim.quickjournal.databinding.FragmentHomeBinding
import com.jim.quickjournal.db.entity.JournalEntry
import com.jim.quickjournal.ui.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, JournalViewModel>(FragmentHomeBinding::inflate) {

    override val viewModel by viewModels<JournalViewModel>()

    //Activity Member Variables
    @Inject
    lateinit var mAdapter: JournalAdapter

    override fun setupUI(view: View, savedInstanceState: Bundle?) {
        super.setupUI(view, savedInstanceState)
        // Set the RecyclerView to its corresponding view
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize the adapter and attach it to the RecyclerView
        binding.recyclerView.adapter = mAdapter
    }

    override fun setupVM() {
        super.setupVM()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadAllJournals().filterNotNull().collect {
                    mAdapter.setJournalsList(
                        context = requireContext(), it
                    )
                    mAdapter.notifyDataSetChanged()
                }
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