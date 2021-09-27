package com.gms.app.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.repo.home.SliderImagesRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeVM  @ViewModelInject
constructor(
    private val sliderImagesRepo: SliderImagesRepo,
) : BaseViewModel() {

    private var jobSlider: Job? = null
    private var _uiStateSlider = MutableLiveData<Event<UiStates>>()
    val uiStateSlider: LiveData<Event<UiStates>>
        get() = _uiStateSlider
    var sliderImages: ArrayList<SliderModel> = arrayListOf()

    fun getSliderImages() {
        if (NetworkUtils.isConnected()) {
            if (jobSlider?.isActive == true)
                return
            jobSlider= launchJobSlider()
        } else {
            _uiStateSlider.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJobSlider(): Job {
        _uiStateSlider.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = sliderImagesRepo.getSliderImages()) {
                is DataResource.Success -> {
                    sliderImages.clear()
                    sliderImages.addAll(response.value)
                    if (sliderImages.size > 0)
                        _uiStateSlider.value = Event(UiStates.Success)
                    else
                        _uiStateSlider.value = Event(UiStates.Empty)
                }
                is DataResource.Error -> {
                    _uiStateSlider.value = Event(UiStates.NotFound)
                }
            }
        }
    }
}