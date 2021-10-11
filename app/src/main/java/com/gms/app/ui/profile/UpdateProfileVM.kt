package com.gms.app.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import com.gms.app.data.storage.remote.model.auth.SignUpBody
import com.gms.app.data.storage.remote.model.profile.ProfileBody
import com.gms.app.repo.StoredDataRepo
import com.gms.app.repo.auth.SignUpRepo
import com.gms.app.repo.profile.UpdateProfileRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UpdateProfileVM @ViewModelInject
constructor(
    private val updateProfileRepo: UpdateProfileRepo,
) : BaseViewModel() {


    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var message: String? = null

    fun updateProfile(body: ProfileBody) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(body)
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(body: ProfileBody): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = updateProfileRepo.updateProfile(body)) {
                is DataResource.Success -> {
                    val signUpResponse = response.value
                    val key: Int = response.value.id
                    message = signUpResponse.message
                    if (key > 0)
                        _uiState.value = Event(UiStates.Success)
                    else _uiState.value = Event(UiStates.Error)

                }
                is DataResource.Error -> {
                    _uiState.value = Event(UiStates.NotFound)
                }
            }
        }
    }

}