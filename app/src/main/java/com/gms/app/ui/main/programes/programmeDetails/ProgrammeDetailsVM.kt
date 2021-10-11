package com.gms.app.ui.main.programes.programmeDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.programs.ProgrammeDetailsModel
import com.gms.app.data.storage.remote.model.programs.ProgrammePeriodModel
import com.gms.app.repo.programs.AddCustomerProgrammeRepo
import com.gms.app.repo.programs.ProgrammeDetailsRepo
import com.gms.app.repo.programs.ProgrammePeriodsRepo
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
    private val programmePeriodsRepo: ProgrammePeriodsRepo,
    private val addCustomerProgrammeRepo: AddCustomerProgrammeRepo,
) : BaseViewModel() {

    var selectedPeriod: Int? = null


    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var programmeId: Int? = null
    val programsDetailsList = ArrayList<ProgrammeDetailsModel>()

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
                    programsDetailsList.clear()
                    programsDetailsList.addAll(response.value)
                    if (programsDetailsList.size > 0)
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


    //Get Programme Periods
    private var jobPeriods: Job? = null
    private var _uiStatePeriods = MutableLiveData<Event<UiStates>>()
    val uiStatePeriods: LiveData<Event<UiStates>>
        get() = _uiStatePeriods
    val programsPeriodsList = ArrayList<ProgrammePeriodModel>()

    fun getProgrammePeriods() {
        if (NetworkUtils.isConnected()) {
            if (jobPeriods?.isActive == true)
                return
            jobPeriods = launchJobPeriods()
        } else {
            _uiStatePeriods.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJobPeriods(): Job {
        _uiStatePeriods.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = programmePeriodsRepo.getProgrammePeriods(programmeId!!)) {
                is DataResource.Success -> {
                    programsPeriodsList.clear()
                    programsPeriodsList.addAll(response.value)
                    if (programsPeriodsList.size > 0)
                        _uiStatePeriods.value = Event(UiStates.Success)
                    else
                        _uiStatePeriods.value = Event(UiStates.Empty)
                }
                is DataResource.Error -> {
                    _uiStatePeriods.value = Event(UiStates.NotFound)
                }
            }
        }
    }

    //Add Customer Programme
    private var jobAdd: Job? = null
    private var _uiStateAdd = MutableLiveData<Event<UiStates>>()
    val uiStateAdd: LiveData<Event<UiStates>>
        get() = _uiStateAdd

    fun addCustomerProgramme() {
        if (NetworkUtils.isConnected()) {
            if (jobAdd?.isActive == true)
                return
            jobAdd = launchJobAdd()
        } else {
            _uiStateAdd.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJobAdd(): Job {
        _uiStateAdd.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response =
                addCustomerProgrammeRepo.addCustomerProgramme(programmeId!!, selectedPeriod!!)) {
                is DataResource.Success -> {
                    if (response.value > 0)
                        _uiStateAdd.value = Event(UiStates.Success)
                    else
                        _uiStateAdd.value = Event(UiStates.Empty)
                }
                is DataResource.Error -> {
                    _uiStateAdd.value = Event(UiStates.NotFound)
                }
            }
        }
    }
}