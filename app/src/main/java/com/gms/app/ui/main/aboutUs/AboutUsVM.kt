package com.gms.app.ui.main.aboutUs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.aboutUs.AboutUsModel
import com.gms.app.data.storage.remote.model.profile.UserDataModel
import com.gms.app.repo.aboutUs.AboutUsRepo
import com.gms.app.repo.profile.ProfileRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AboutUsVM  @ViewModelInject
constructor(
    private val aboutUsRepo: AboutUsRepo,
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var aboutUsModel: AboutUsModel? = null

    fun getAboutUsData() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = aboutUsRepo.getAboutUsData()) {
                is DataResource.Success -> {
                    aboutUsModel = response.value
                    if (aboutUsModel != null)
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