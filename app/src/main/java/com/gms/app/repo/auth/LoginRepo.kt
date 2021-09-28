package com.gms.app.repo.auth


import com.blankj.utilcode.util.DeviceUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.addKeyPropertyString
import com.gms.app.utils.safeApiCall
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject


class LoginRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private lateinit var model: AuthResponse

    suspend fun doLogin(email: String, password: String): DataResource<AuthResponse> {
        return safeApiCall { login(email, password) }
    }

    private suspend fun login(
        email: String, password: String
    ): AuthResponse {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.Login.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyString(request, "Password", password)
        addKeyPropertyString(request, "Email", email)
        addKeyPropertyString(request, "UID", preferencesHelper.uid)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.Login.value),
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