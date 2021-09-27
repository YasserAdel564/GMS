package com.gms.app.repo.auth

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.data.storage.remote.model.auth.SignUpBody
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class SignUpRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {
    private lateinit var model: AuthResponse

    suspend fun postSignUp(body: SignUpBody): DataResource<AuthResponse> {
        return safeApiCall { signUp(body) }
    }

    private fun signUp(
        body: SignUpBody
    ): AuthResponse {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.SignUp.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyString(request ,"FullName", body.fullName.toString())
        addKeyPropertyString(request ,"Dateofbirth", body.birthDate.toString())
        addKeyPropertyString(request ,"Email", body.email.toString())
        addKeyPropertyString(request ,"Mobile", body.phone.toString())
        addKeyPropertyString(request ,"Contact", body.contact.toString())
        addKeyPropertyInt(request ,"ID_Gender", body.genderId)
        addKeyPropertyInt(request ,"ID_Nationality", body.nationalityId)
        addKeyPropertyInt(request ,"ID_Country", body.countryId)
        addKeyPropertyInt(request ,"ID_Living", body.livingId)
        addKeyPropertyString(request ,"Education", body.education.toString())
        addKeyPropertyString(request ,"Universty", body.unversity.toString())
        addKeyPropertyInt(request ,"Graduationyear", body.graduationYear)
        addKeyPropertyString(request ,"Fulladdress", body.address.toString())
        addKeyPropertyString(request ,"Notes", body.note.toString())
        addKeyPropertyString(request ,"Password", body.password.toString())
        addKeyPropertyString(request ,"UID", body.uid.toString())
        addKeyPropertyString(request ,"GenderName", body.genderName.toString())
        addKeyPropertyString(request ,"NationalityName", body.nationalityName.toString())
        addKeyPropertyString(request ,"LivingName", body.livingName.toString())

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.SignUp.value),
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
            Log.e("id",id)
            Log.e("message",message)
        }
        return model
    }



}