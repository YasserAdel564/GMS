package com.gms.app.ui.main.programes.programmeDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.programs.ProgrammePeriodModel
import com.gms.app.databinding.ProgrammeDetailsFragmentBinding
import com.gms.app.ui.main.MainVM
import com.gms.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class ProgrammeDetailsFragment : Fragment() {

    private val mainVM: MainVM by activityViewModels()
    private val viewModel: ProgrammeDetailsVM by activityViewModels()
    lateinit var binding: ProgrammeDetailsFragmentBinding
    private var periodsArray = ArrayList<ProgrammePeriodModel>()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

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
        viewModel.uiStatePeriods.observeEvent(viewLifecycleOwner) { onPeriodsResponse(it) }
        viewModel.uiStateAdd.observeEvent(viewLifecycleOwner) { onAddProgrammeResponse(it) }
        mainVM.programmeId.observe(viewLifecycleOwner, {
            viewModel.programmeId = it
            viewModel.getProgrammeDetails()
            viewModel.getProgrammePeriods()
        })
        setUpToolbar()
        setUpViewsClicks()
    }

    private fun addCustomerProgramme() {
        if (preferencesHelper.isLogin)
            viewModel.addCustomerProgramme()
        else
            activity?.snackBarWithAction(
                requireActivity().resources.getString(R.string.should_login),
                requireActivity().resources.getString(R.string.login_label), binding.viewRoot
            ) { findNavController().navigate(R.id.login_fragment) }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.programme_detils)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
        binding.addProgrammeBtn.setOnClickListener { addCustomerProgramme() }
        binding.periodsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        viewModel.selectedPeriod = periodsArray[position].id
                        binding.addProgrammeBtn.visibility = View.VISIBLE
                    }
                }
            }

    }

    private fun onPeriodsResponse(states: UiStates?) {
        when (states) {
            UiStates.Success -> {
                renderPeriodsData()
            }
            UiStates.Empty -> {
                binding.periodsSpinner.visibility = View.GONE
            }
        }
    }


    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                viewInputs()
                fillViews()
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

    private fun onAddProgrammeResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                viewInputs()
                activity?.snackBar(
                    requireActivity().resources.getString(R.string.success),
                    binding.viewRoot
                )
                findNavController().navigateUp()
            }
            UiStates.Empty -> {
                viewInputs()
                activity?.snackBar(
                    requireActivity().resources.getString(R.string.error_general),
                    binding.viewRoot
                )
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }


    private fun renderPeriodsData() {
        periodsArray.clear()
        periodsArray.add(
            0,
            ProgrammePeriodModel(0, requireActivity().resources.getString(R.string.select_period))
        )
        periodsArray.addAll(viewModel.programsPeriodsList)
        if (viewModel.programsPeriodsList.isNotEmpty()) {
            val spinnerArrayAdapter = MyArrayAdapter(requireContext(), periodsArray, true)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_view)
            binding.periodsSpinner.adapter = spinnerArrayAdapter
        }
    }

    private fun fillViews() {
        val model = viewModel.programsDetailsList[0]
        Glide.with(requireActivity()).load(model.img)
            .placeholder(R.drawable.app_logo)
            .into(binding.programmeImg)
        binding.programmeTitleTxt.text = model.title
        binding.programmeSummaryTxt.text = model.summary
        binding.programmeDetails1Txt.text = getStringFromHtml(model.details1)
        binding.programmeDetails2Txt.text = getStringFromHtml(model.details2)
    }

    private fun onLoading() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun onEmpty() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.empty.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
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