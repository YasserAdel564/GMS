package com.gms.app.repo.programs


import android.util.Log
import com.blankj.utilcode.util.DeviceUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject
import kotlin.properties.Delegates


class AddCustomerProgrammeRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {
    private var response by Delegates.notNull<Int>()


    suspend fun addCustomerProgramme(programmeId: Int, periodId: Int): DataResource<Int> {
        return safeApiCall { addCustomerProgrammeCall(programmeId, periodId) }
    }

    private suspend fun addCustomerProgrammeCall(
        programmeId: Int, periodId: Int
    ): Int {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.AddCustomerProgramme.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "ID_Customers", preferencesHelper.userId.toInt())
        addKeyPropertyInt(request, "ID_Diplomas", programmeId)
        addKeyPropertyInt(request, "ID_Periods", periodId)


        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.AddCustomerProgramme.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val id: String = soapObject.getProperty("ID").toString()
            response =id.toInt()
        }
        return response
    }


}