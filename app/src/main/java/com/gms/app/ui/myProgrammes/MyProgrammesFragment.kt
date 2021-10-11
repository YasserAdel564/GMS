package com.gms.app.ui.myProgrammes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.data.storage.remote.model.programs.MyProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.databinding.MyProgrammesFragmentBinding
import com.gms.app.databinding.ProfileFragmentBinding
import com.gms.app.ui.main.programes.ProgrammesAdapter
import com.gms.app.ui.main.programes.ProgramsVM
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProgrammesFragment : Fragment() {

    lateinit var binding: MyProgrammesFragmentBinding
    private val viewModel: MyProgrammesVM by viewModels()
    private var adapter: MyProgrammesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyProgrammesFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        viewModel.getMyPrograms()
        setUpRV()
        setUpToolbar()
        setUpViewsClicks()
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.my_programmes_label)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setUpRV() {
        adapter = MyProgrammesAdapter(requireActivity())
        binding.myProgramsRv.adapter = adapter
    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                adapter?.renderData(viewModel.myProgramsList)
                viewInputs()
            }
            UiStates.Empty -> {
                onEmpty()
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onLoading() {
        binding.myProgramsRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.myProgramsRv.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onEmpty() {
        binding.myProgramsRv.visibility = View.GONE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.empty.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.myProgramsRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
    }


    private fun onNoConnection() {
        binding.myProgramsRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }

}