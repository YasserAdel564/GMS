package com.gms.app.ui

import androidx.hilt.lifecycle.ViewModelInject
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.repo.StoredDataRepo
import com.gms.app.repo.auth.CountriesRepo
import com.gms.app.repo.auth.GendersRepo
import com.gms.app.repo.auth.NationalitiesRepo
import kotlinx.coroutines.*

class LauncherViewModel @ViewModelInject
constructor(
    private val preferencesHelper: PreferencesHelper,
    private val countriesRepo: CountriesRepo,
    private val genderRepo: GendersRepo,
    private val nationalitiesRepo: NationalitiesRepo,
    private val storedDataRepo: StoredDataRepo,
) : BaseViewModel() {
    init {
        CoroutineScope(Dispatchers.Main).launch {
            genderRepo.getGender()
            countriesRepo.getCountries()
            nationalitiesRepo.getNationalities()
        }
    }

    fun clearDB() {
        storedDataRepo.clearDB()
    }
}