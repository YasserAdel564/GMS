package com.gms.app.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.databinding.HomeFragmentBinding
import com.gms.app.ui.main.programes.ProgrammesAdapter
import com.gms.app.ui.main.programes.ProgramsVM
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class HomeFragment : Fragment() ,ProgrammesAdapter.ProgrammeCallback {

    lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeVM by viewModels()
    private val programsVM: ProgramsVM by viewModels()

    private var timer: Timer = Timer()
    private val sliderAdapter = SliderAdapter()
    private var adapter: ProgrammesAdapter? = null


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
        adapter = ProgrammesAdapter(requireActivity(),this)
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

    override fun programmeClicked(model: ProgrammeModel?) {
        Log.e("programmeClicked", model?.id.toString())
    }

}