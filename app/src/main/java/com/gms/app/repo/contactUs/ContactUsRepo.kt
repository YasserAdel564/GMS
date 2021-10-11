package com.gms.app.repo.contactUs

import android.util.Log
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.addKeyPropertyString
import com.gms.app.utils.safeApiCall
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject


class ContactUsRepo @Inject
constructor(
) {

    private lateinit var model: AuthResponse

    suspend fun doContactUs(
        name: String,
        email: String,
        phone: String,
        title: String,
        message: String
    ): DataResource<AuthResponse> {
        return safeApiCall { contactUs(name, email, phone, title, message) }
    }

    private fun contactUs(
        name: String,
        email: String,
        phone: String,
        title: String,
        message: String
    ): AuthResponse {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.ContactUs.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyString(request, "Name", name)
        addKeyPropertyString(request, "Email", email)
        addKeyPropertyString(request, "Tel", phone)
        addKeyPropertyString(request, "Title", title)
        addKeyPropertyString(request, "Message",message)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.ContactUs.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val id: String = soapObject.getProperty("ID").toString()
            val message: String = soapObject.getProperty("MS").toString()
            model = AuthResponse(id = id.toInt(), message = message)
        }
        return model
    }


}