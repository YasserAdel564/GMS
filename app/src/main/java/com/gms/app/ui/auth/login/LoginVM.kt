package com.gms.app.ui.auth.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.repo.auth.LoginRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginVM  @ViewModelInject
constructor(
    private val repository: LoginRepo,
    private val preferencesHelper: PreferencesHelper
) : BaseViewModel() {

    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState

    var infoMessage: String? = null

    fun login(email: String, password: String) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(email, password)
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(email: String, password: String): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = repository.doLogin( email, password)) {
                is DataResource.Success -> {
                    val loginResponse = response.value
                    val key: Int = response.value.id.toInt()
                    infoMessage = loginResponse.message
                    when {
                        key > 0 -> {
                            _uiState.value = Event(UiStates.Success)
                            preferencesHelper.userId = key.toString()
                            preferencesHelper.isLogin = true
                        }
                        key == -4 -> _uiState.value = Event(UiStates.Empty)
                        else -> _uiState.value = Event(UiStates.Error)
                    }
                }
                is DataResource.Error -> {
                    _uiState.value = Event(UiStates.NotFound)
                }
            }
        }
    }
}