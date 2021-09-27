package com.gms.app.ui.main.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gms.app.R
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.databinding.HomeFragmentBinding
import com.gms.app.databinding.MainFragmentBinding
import com.gms.app.ui.main.programes.ProgramsAdapter
import com.gms.app.ui.main.programes.ProgramsVM
import com.gms.app.utils.TestList
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeVM by viewModels()
    private val programsVM: ProgramsVM by viewModels()

    private var timer: Timer = Timer()
    private val sliderAdapter = SliderAdapter()
    private var adapter: ProgramsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiStateSlider.observeEvent(viewLifecycleOwner) { onSliderResponse(it) }
        programsVM.uiState.observeEvent(viewLifecycleOwner) { onProgramsResponse(it) }
        viewModel.getSliderImages()
        programsVM.getAllPrograms()
        setUpVP()
        setUpRV()
    }



    private fun setUpRV() {
        adapter = ProgramsAdapter(requireActivity())
        binding.homeRv.adapter = adapter
    }

    private fun setUpVP() {
        binding.mainSliderVp.adapter = sliderAdapter
        binding.mainSliderVp.setCurrentItem(0, true)
        binding.pageIndicator.setViewPager(binding.mainSliderVp)
    }

    private fun renderSliderData() {
        sliderAdapter.submitList(viewModel.sliderImages)
    }

    private fun autoSlide() {
        timer.cancel()
        timer = Timer()
        timer.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (binding.mainSliderVp.currentItem < sliderAdapter.count - 1) {
                    binding.mainSliderVp.setCurrentItem(
                        binding.mainSliderVp.currentItem + 1,
                        true
                    )
                } else {
                    binding.mainSliderVp.setCurrentItem(0, true)
                }

            }
        }, 5000, 5000)
    }
    private fun onSliderResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                renderSliderData()
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

    private fun onProgramsResponse(states: UiStates?) {
        when (states) {
            UiStates.Success -> {
                adapter?.renderData(programsVM.programsList)
                viewInputs()
            }
            UiStates.Empty -> {
                viewInputs()
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


    override fun onResume() {
        super.onResume()
        autoSlide()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}