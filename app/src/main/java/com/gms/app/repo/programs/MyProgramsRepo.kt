package com.gms.app.repo.programs

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.data.storage.remote.model.programs.MyProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammePeriodModel
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class MyProgramsRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private val myProgramsList = ArrayList<MyProgrammeModel>()

    suspend fun getMyPrograms(): DataResource<List<MyProgrammeModel>> {
        return safeApiCall { myProgramsCall() }
    }

    private fun myProgramsCall(): List<MyProgrammeModel> {
        myProgramsList.clear()
        val request =
            SoapObject(Constants.NameSpace, Constants.MethodNames.GetCustomerProgramme.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "ID", preferencesHelper.userId.toInt())

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetCustomerProgramme.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            myProgramsList.clear()
            val tables = object1.getProperty(0) as SoapObject
            for (i in 0 until tables.propertyCount) {
                val soapObject = tables.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val title: String = soapObject.getProperty("Diplomas_Title").toString()
                val summary: String = soapObject.getProperty("Diplomas_Summery").toString()
                val img: String = soapObject.getProperty("Diplomas_Img").toString()
                val period: String = soapObject.getProperty("Periods").toString()
                val numberOfHours: String = soapObject.getProperty("Numberhours").toString()
                val numberOfSeats: String = soapObject.getProperty("NumberSeats").toString()
                val startDate: String = soapObject.getProperty("Startdate").toString()
                myProgramsList.add(
                    (MyProgrammeModel(
                        id = id.toInt(),
                        title = title,
                        summary = summary,
                        img = Constants.ImagesUrl + img,
                        period = period,
                        numberOfHours = numberOfHours,
                        numberOfSeats = numberOfSeats, startDate = startDate
                    ))
                )
            }
        }
        return myProgramsList
    }

}