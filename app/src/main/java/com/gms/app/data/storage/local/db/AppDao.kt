package com.gms.app.data.storage.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel

@Dao
interface AppDao {

    //Insertion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenders(countries: List<GenderModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: List<CountryModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNationalities(countries: List<NationalityModel>)


    //Getting
    @Query("SELECT * FROM genders ORDER BY name")
    fun getStoredLiveGenders(): LiveData<List<GenderModel>>

    @Query("SELECT * FROM countries ORDER BY name")
    fun getStoredLiveCountries(): LiveData<List<CountryModel>>

    @Query("SELECT * FROM nationalities ORDER BY name")
    fun getStoredLiveNationalities(): LiveData<List<NationalityModel>>


    //Delete
    @Query("DELETE FROM genders")
    fun clearGenders()

    @Query("DELETE FROM countries")
    fun clearCountries()

    @Query("DELETE FROM nationalities")
    fun clearNationalities()


}