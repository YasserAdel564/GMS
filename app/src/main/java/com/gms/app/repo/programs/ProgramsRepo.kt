package com.gms.app.repo.programs

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.gms.app.utils.soapRequestBuilder
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class ProgramsRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private val programsList = ArrayList<ProgrammeModel>()

    suspend fun getAllPrograms(): DataResource<List<ProgrammeModel>> {
        return safeApiCall { programsCall() }
    }

    private fun programsCall(): List<ProgrammeModel> {
        programsList.clear()
        val table =
            soapRequestBuilder(
                Constants.MethodNames.GetAllPrograms.value,
                preferencesHelper.language
            )
        if (table != null) {
            for (i in 0 until table.propertyCount) {
                val soapObject = table.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val summary: String = soapObject.getProperty("Diplomas_Summery").toString()
                val title: String = soapObject.getProperty("Diplomas_Title").toString()
                val img: String = soapObject.getProperty("Diplomas_Img").toString()
                programsList.add(
                    (ProgrammeModel(
                        id = id.toInt(),
                        summary = summary,
                        title = title,
                        img = Constants.ImagesUrl + img
                    ))
                )
            }

        }
        return programsList
    }

}