package com.gms.app.ui.main.programes.programmeDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.databinding.ProgrammeDetailsFragmentBinding
import com.gms.app.ui.main.MainVM
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgrammeDetailsFragment : Fragment() {

    private val mainVM: MainVM by activityViewModels()
    private val viewModel: ProgrammeDetailsVM by activityViewModels()
    lateinit var binding: ProgrammeDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProgrammeDetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        mainVM.programmeId.observe(viewLifecycleOwner, {
            viewModel.programmeId = it
            viewModel.getProgrammeDetails()
        })
        setUpToolbar()
        setUpViewsClicks()
    }
    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text = requireActivity().resources.getString(R.string.programme_detils)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
               // adapter?.renderData(viewModel.programsList)
                viewInputs()
            }
            UiStates.Empty -> {
                viewInputs()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onLoading() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.viewContainer.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onNoConnection() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }
}