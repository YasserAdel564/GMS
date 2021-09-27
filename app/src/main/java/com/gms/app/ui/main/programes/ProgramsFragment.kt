package com.gms.app.ui.main.programes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gms.app.databinding.ProgramsFragmentBinding
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgramsFragment : Fragment() {

    lateinit var binding: ProgramsFragmentBinding
    private val viewModel: ProgramsVM by viewModels()
    private var adapter: ProgramsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProgramsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        viewModel.getAllPrograms()
        setUpRV()
    }

    private fun setUpRV() {
        adapter = ProgramsAdapter(requireActivity())
        binding.programsRv.adapter = adapter
    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                adapter?.renderData(viewModel.programsList)
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
        binding.programsRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.programsRv.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onNoConnection() {
        binding.programsRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }


}