package com.gms.app.repo.profile


import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.data.storage.remote.model.profile.ProfileBody
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class UpdateProfileRepo @Inject
constructor() {

    private lateinit var model: AuthResponse

    suspend fun updateProfile(body: ProfileBody): DataResource<AuthResponse> {
        return safeApiCall { updateProfileCall(body) }
    }

    private fun updateProfileCall(
        body: ProfileBody
    ): AuthResponse {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.UpdateProfile.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "PID", body.userId.toInt())
        addKeyPropertyString(request, "FullName", body.name)
        addKeyPropertyString(request, "Dateofbirth", body.birthDate)
        addKeyPropertyString(request, "Email", body.email)
        addKeyPropertyString(request, "Mobile", body.phone)
        addKeyPropertyString(request, "Contact", body.contact)
        addKeyPropertyInt(request, "ID_Gender", body.genderId)
        addKeyPropertyInt(request, "ID_Nationality", body.nationalityId)
        addKeyPropertyInt(request, "ID_Country", body.countryId)
        addKeyPropertyInt(request, "ID_Living", body.livingId)
        addKeyPropertyString(request, "Education", body.education)
        addKeyPropertyString(request, "Universty", body.university)
        addKeyPropertyInt(request, "Graduationyear", body.graduationYear)
        addKeyPropertyString(request, "Fulladdress", body.address)
        addKeyPropertyString(request, "Notes", body.note)
        addKeyPropertyString(request, "Password", body.password)
        addKeyPropertyString(request, "picture", body.img)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.UpdateProfile.value),
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