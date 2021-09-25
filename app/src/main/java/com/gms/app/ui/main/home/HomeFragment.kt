package com.gms.app.ui.main.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gms.app.R
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.databinding.HomeFragmentBinding
import com.gms.app.databinding.MainFragmentBinding
import com.gms.app.utils.TestList
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: HomeFragmentBinding
    private var timer: Timer = Timer()
    private val sliderAdapter = SliderAdapter()
    private val slider = ArrayList<SliderModel>()
    private var adapter: DepartmentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpVP()
        setUpRV()
        renderSliderData()
    }



    private fun setUpRV() {
        adapter = DepartmentsAdapter(requireActivity(),
            TestList.ITEMS as ArrayList<TestList.TestItem>
        )
        binding.homeRv.adapter = adapter
    }

    private fun setUpVP() {
        binding.mainSliderVp.adapter = sliderAdapter
        binding.mainSliderVp.setCurrentItem(0, true)
        binding.pageIndicator.setViewPager(binding.mainSliderVp)
    }

    private fun renderSliderData() {
        slider.add(
            0,
            SliderModel(
                "Best Medical Courses , Show it Now !",
                "https://149606532.v2.pressablecdn.com/wp-content/uploads/2021/06/Best-Courses-Medical-Field.png",
                "",
                "",
                "",
                "",
                ""
            )
        )
        slider.add(
            1,
            SliderModel(
                "Best Medical Courses , Show it Now !",
                "http://worldscholarshipforum.com/wp-content/uploads/2021/04/Best-Medical-Scholarships-For-African-Students.jpg",
                "",
                "",
                "",
                "",
                ""
            )
        )
        slider.add(
            2,
            SliderModel(
                "Best Medical Courses , Show it Now !",
                "https://worldscholarshipforum.com/wp-content/uploads/2021/02/Medical-School-Scholarships-for-International-Students-In-USA-1.jpg",
                "",
                "",
                "",
                "",
                ""
            )
        )
        slider.add(
            3,
            SliderModel(
                "Best Medical Courses , Show it Now !",
                "https://media.mehrnews.com/d/2018/12/02/4/2972804.jpg",
                "",
                "",
                "",
                "",
                ""
            )
        )

        sliderAdapter.submitList(slider)

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

    override fun onResume() {
        super.onResume()
        autoSlide()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}