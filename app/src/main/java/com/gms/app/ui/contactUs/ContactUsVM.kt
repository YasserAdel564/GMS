package com.gms.app.ui.contactUs

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.repo.auth.LoginRepo
import com.gms.app.repo.contactUs.ContactUsRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContactUsVM @ViewModelInject
constructor(
    private val repository: ContactUsRepo,
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState

    var infoMessage: String? = null

    fun contactUs(
        name: String,
        email: String,
        phone: String,
        title: String,
        message: String
    ) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(name, email, phone, title, message)
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(
        name: String,
        email: String,
        phone: String,
        title: String,
        message: String
    ): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = repository.doContactUs(name, email, phone, title, message)) {
                is DataResource.Success -> {
                    val loginResponse = response.value
                    val key: Int = response.value.id
                    infoMessage = loginResponse.message
                    if (key > 0)
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