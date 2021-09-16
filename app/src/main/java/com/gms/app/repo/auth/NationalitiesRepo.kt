package com.gms.app.repo.auth

import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.gms.app.utils.soapRequestBuilder
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class NationalitiesRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper,
    private val db: AppDao
) {

    private val nationalitiesList = ArrayList<NationalityModel>()

    suspend fun getNationalities(): DataResource<Boolean> {
        return safeApiCall { nationalities() }
    }

    private fun nationalities(): Boolean {
        val table = soapRequestBuilder(Constants.MethodNames.GetNationality.value, preferencesHelper.language)
        if (table !=null) {
            for (i in 0 until table.propertyCount) {
                val soapObject = table.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val name: String = soapObject.getProperty("Nationalty").toString()
                nationalitiesList.add(
                    (NationalityModel(
                        id = id.toInt(),
                        name = name
                    ))
                )
            }
            db.clearNationalities()
            db.insertNationalities(nationalitiesList)
        }
        return true
    }

}