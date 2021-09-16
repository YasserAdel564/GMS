package com.gms.app.repo

import androidx.lifecycle.LiveData
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import javax.inject.Inject

class StoredDataRepo @Inject
constructor(
    private val db: AppDao
) {

    //Fetch
    fun getStoredGenders(): LiveData<List<GenderModel>> {
        return db.getStoredLiveGenders()
    }
    fun getStoredCountries(): LiveData<List<CountryModel>> {
        return db.getStoredLiveCountries()
    }
    fun getStoredNationalities(): LiveData<List<NationalityModel>> {
        return db.getStoredLiveNationalities()
    }

    //Delete
    fun clearDB() {
        db.clearGenders()
        db.clearCountries()
        db.clearNationalities()
    }

}