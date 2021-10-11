package com.gms.app.ui.videos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.programs.MyProgrammeModel
import com.gms.app.data.storage.remote.model.video.VideoModel
import com.gms.app.repo.programs.MyProgramsRepo
import com.gms.app.repo.videos.VideosRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VideosVM  @ViewModelInject
constructor(
    private val videosRepo: VideosRepo,
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var videosList: ArrayList<VideoModel> = arrayListOf()

    fun getVideos() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job= launchJob()
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = videosRepo.getVideos()) {
                is DataResource.Success -> {
                    videosList.clear()
                    videosList.addAll(response.value)
                    if (videosList.size > 0)
                        _uiState.value = Event(UiStates.Success)
                    else
                        _uiState.value = Event(UiStates.Empty)
                }
                is DataResource.Error -> {
                    _uiState.value = Event(UiStates.NotFound)
                }
            }
        }
    }
}