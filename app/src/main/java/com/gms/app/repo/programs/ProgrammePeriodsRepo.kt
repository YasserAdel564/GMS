package com.gms.app.repo.programs

import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.programs.ProgrammeDetailsModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammePeriodModel
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class ProgrammePeriodsRepo @Inject
constructor() {

    private val programsPeriodsList = ArrayList<ProgrammePeriodModel>()

    suspend fun getProgrammePeriods(programmeId: Int): DataResource<List<ProgrammePeriodModel>> {
        return safeApiCall { programmePeriods(programmeId) }
    }

    private fun programmePeriods(programmeId: Int): List<ProgrammePeriodModel> {
        val request =
            SoapObject(Constants.NameSpace, Constants.MethodNames.GetProgrammePeriods.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "ID_Diplomas", programmeId)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetProgrammePeriods.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            programsPeriodsList.clear()
            val tables = object1.getProperty(0) as SoapObject
            for (i in 0 until tables.propertyCount) {
                val soapObject = tables.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val period: String = soapObject.getProperty("Periods").toString()
                programsPeriodsList.add(
                    (ProgrammePeriodModel(
                        id = id.toInt(),
                        period = period
                    ))
                )
            }
        }
        return programsPeriodsList
    }


}