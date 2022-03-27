package com.jim.quickjournal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
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
class HomeFragment : Fragment() {

    private val viewModel: JournalViewModel by viewModels()


    //Activity Member Variables
    @Inject
    lateinit var mAdapter: JournalAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    internal var view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        // Set the RecyclerView to its corresponding view
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize the adapter and attach it to the RecyclerView
        binding.recyclerView.adapter = mAdapter
        setUpViewModel()

    }

    private fun setUpViewModel() {
        lifecycle.coroutineScope.launch {
            viewModel.loadAllJournals().collect {
                mAdapter.setJournalsList(
                    context = requireContext(),
                    it
                )
                mAdapter.notifyDataSetChanged()
            }
        }

//        viewModel.journalList.observe(viewLifecycleOwner) {
//
//        }
        mAdapter.setOnItemClickListener { _, any2, _ ->
            val mJournalEntry = any2 as JournalEntry
            val args = Bundle()
            args.putInt(JournalDetailFragment.EXTRA_JOURNAL_ID, mJournalEntry.id)
            this.findNavController().navigate(R.id.action_nav_to_journalDetailFragment, args)
        }
    }

}