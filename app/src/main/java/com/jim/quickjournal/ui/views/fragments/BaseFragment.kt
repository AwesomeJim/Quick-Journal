package com.jim.quickjournal.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.jim.quickjournal.extentions.Inflate
import com.jim.quickjournal.ui.activities.MainViewsActivity

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    protected abstract val viewModel: VM

    // Activity
    protected val mainActivity: MainViewsActivity? by lazy {
        activity as? MainViewsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view, savedInstanceState)
        setupVM()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Handle UI
     */
    protected open fun setupUI(view: View, savedInstanceState: Bundle?) = Unit

    /**
     * Handle view model observers
     */
    protected open fun setupVM() {
        viewModel.finishRequest.observe(viewLifecycleOwner) {
            mainActivity?.finish()
        }
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            it(findNavController())
        }
    }
}