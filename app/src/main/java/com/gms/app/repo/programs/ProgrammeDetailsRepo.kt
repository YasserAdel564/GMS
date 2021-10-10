package com.gms.app.repo.programs

import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.programs.ProgrammeDetailsModel
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class ProgrammeDetailsRepo @Inject
constructor() {

    private lateinit var model: ProgrammeDetailsModel

    suspend fun getProgrammeDetails(programmeId: Int): DataResource<ProgrammeDetailsModel> {
        return safeApiCall { programmeDetails(programmeId) }
    }

    private fun programmeDetails(programmeId: Int): ProgrammeDetailsModel {
        val request =
            SoapObject(Constants.NameSpace, Constants.MethodNames.GetProgrammeDetails.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "ID", programmeId)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetProgrammeDetails.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val id: String = soapObject.getProperty("ID").toString()
            val title: String = soapObject.getProperty("Diplomas_Title").toString()
            val summary: String = soapObject.getProperty("Diplomas_Summery").toString()
            val img: String = soapObject.getProperty("Diplomas_Details1").toString()
            val details1: String = soapObject.getProperty("Diplomas_Details2").toString()
            val details2: String = soapObject.getProperty("Diplomas_Details3").toString()
            val details3: String = soapObject.getProperty("Diplomas_Details4").toString()
            val details4: String = soapObject.getProperty("Diplomas_Img").toString()
            model = ProgrammeDetailsModel(
                id = id.toInt(),
                title = title,
                summary = summary,
                img = img,
                details1 = details1,
                details2 = details2,
                details3 = details3,
                details4 = details4
            )
        }
        return model
    }


}