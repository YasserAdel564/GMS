package com.gms.app.repo.auth

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.gms.app.utils.soapRequestBuilder
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class CountriesRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper,
    private val db: AppDao
) {

    private val countriesList = ArrayList<CountryModel>()

    suspend fun getCountries(): DataResource<Boolean> {
        return safeApiCall { countries() }
    }

    private fun countries(): Boolean {
        val table = soapRequestBuilder(Constants.MethodNames.GetCountries.value, preferencesHelper.language)
        if (table !=null) {
            for (i in 0 until table.propertyCount) {
                val soapObject = table.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val name: String = soapObject.getProperty("Country").toString()
                countriesList.add(
                    (CountryModel(
                        id = id.toInt(),
                        name = name
                    ))
                )
            }
            db.clearCountries()
            db.insertCountries(countriesList)
        }
        return true
    }

}