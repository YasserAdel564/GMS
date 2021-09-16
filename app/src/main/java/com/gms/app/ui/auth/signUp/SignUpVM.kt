package com.gms.app.ui.auth.signUp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import com.gms.app.data.storage.remote.model.auth.SignUpBody
import com.gms.app.repo.StoredDataRepo
import com.gms.app.repo.auth.SignUpRepo
import com.gms.app.ui.BaseViewModel
import com.gms.app.utils.DataResource
import com.gms.app.utils.Event
import com.gms.app.utils.UiStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpVM @ViewModelInject
constructor(
    storedDataRepo: StoredDataRepo,
    val signUpRepo: SignUpRepo
) : BaseViewModel() {

    var storedCountries: LiveData<List<CountryModel>> = storedDataRepo.getStoredCountries()
    var storedLiving: LiveData<List<CountryModel>> = storedDataRepo.getStoredCountries()
    var storedGender: LiveData<List<GenderModel>> = storedDataRepo.getStoredGenders()
    var storedNationalities: LiveData<List<NationalityModel>> =
        storedDataRepo.getStoredNationalities()

    var selectedGender: GenderModel? = null
    var selectedCountry: CountryModel? = null
    var selectedLiving: CountryModel? = null
    var selectedNationality: NationalityModel? = null
    var selectedDateOfBirth: String? = null


    private var job: Job? = null
    private var _uiState = MutableLiveData<Event<UiStates>>()
    val uiState: LiveData<Event<UiStates>>
        get() = _uiState
    var message: String? = null

    fun signUp(body: SignUpBody) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(body)
        } else {
            _uiState.value = Event(UiStates.NoConnection)
        }
    }

    private fun launchJob(body: SignUpBody): Job {
        _uiState.value = Event(UiStates.Loading)
        return CoroutineScope(Dispatchers.Main).launch {
            when (val response = signUpRepo.postSignUp(body)) {
                is DataResource.Success -> {
                    val signUpResponse = response.value
                    val key: Int = response.value.id
                    message = signUpResponse.message
                    when {
                        key > 0 -> {
                            _uiState.value = Event(UiStates.Success)
                        }
                        key == (-4) -> _uiState.value = Event(UiStates.Empty)
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