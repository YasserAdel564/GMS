package com.gms.app.repo.auth

import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.gms.app.utils.soapRequestBuilder
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class GendersRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper,
    private val db: AppDao
) {

    private val genderList = ArrayList<GenderModel>()

    suspend fun getGender(): DataResource<Boolean> {
        return safeApiCall { gender() }
    }

    private fun gender(): Boolean {
        val table = soapRequestBuilder(Constants.MethodNames.GetGender.value, preferencesHelper.language)
        if (table !=null) {
            for (i in 0 until table.propertyCount) {
                val soapObject = table.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val name: String = soapObject.getProperty("Gender").toString()
                genderList.add(
                    (GenderModel(
                        id = id.toInt(),
                        name = name
                    ))
                )
            }
            db.clearGenders()
            db.insertGenders(genderList)
        }
        return true
    }

}