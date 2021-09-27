package com.gms.app.ui.main.programes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.repo.home.SliderImagesRepo
import com.gms.app.repo.programs.ProgramsRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProgramsVM @ViewModelInject
constructor(
    private val programsRepo: ProgramsRepo,
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var programsList: ArrayList<ProgrammeModel> = arrayListOf()

    fun getAllPrograms() {
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
            when (val response = programsRepo.getAllPrograms()) {
                is DataResource.Success -> {
                    programsList.clear()
                    programsList.addAll(response.value)
                    if (programsList.size > 0)
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