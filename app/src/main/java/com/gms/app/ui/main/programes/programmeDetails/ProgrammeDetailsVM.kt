package com.gms.app.ui.main.programes.programmeDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.programs.ProgrammeDetailsModel
import com.gms.app.repo.programs.ProgrammeDetailsRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProgrammeDetailsVM @ViewModelInject
constructor(
    private val programmeDetailsRepo: ProgrammeDetailsRepo,
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var programmeId: Int? = null
    var programmeDetailsModel: ProgrammeDetailsModel? = null

    fun getProgrammeDetails() {
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
            when (val response = programmeDetailsRepo.getProgrammeDetails(programmeId!!)) {
                is DataResource.Success -> {
                    programmeDetailsModel = response.value
                    if (programmeDetailsModel != null)
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