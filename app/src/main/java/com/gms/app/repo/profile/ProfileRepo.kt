package com.gms.app.repo.profile


import com.blankj.utilcode.util.DeviceUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.AuthResponse
import com.gms.app.data.storage.remote.model.profile.UserDataModel
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


class ProfileRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private lateinit var model: UserDataModel

    suspend fun getUserData(): DataResource<UserDataModel> {
        return safeApiCall { userData() }
    }

    private fun userData(): UserDataModel {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.GetUserData.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyString(request, "ID", preferencesHelper.userId)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetUserData.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val id: String = soapObject.getProperty("ID").toString()
            val code: String = soapObject.getProperty("Code").toString()
            val password: String = soapObject.getProperty("Password").toString()
            val email: String = soapObject.getProperty("Email").toString()
            val genderId: String = soapObject.getProperty("ID_Gender").toString()
            val countryId: String = soapObject.getProperty("ID_Country").toString()
            val picture: String = soapObject.getProperty("picture").toString()
            val status: String = soapObject.getProperty("Status").toString()
            val signUpDate: String = soapObject.getProperty("SignupDate").toString()
            val lastInteractionDate: String = soapObject.getProperty("LastInteraction").toString()
            val activationCode: String = soapObject.getProperty("ActivationCode").toString()
            val fullName: String = soapObject.getProperty("FullName").toString()
            val dateOfBirth: String = soapObject.getProperty("Dateofbirth").toString()
            val phone: String = soapObject.getProperty("Mobile").toString()
            val contact: String = soapObject.getProperty("Contact").toString()
            val nationalityId: String = soapObject.getProperty("ID_Nationality").toString()
            val livingId: String = soapObject.getProperty("ID_Living").toString()
            val education: String = soapObject.getProperty("Education").toString()
            val university: String = soapObject.getProperty("Universty").toString()
            val graduationYear: String = soapObject.getProperty("Graduationyear").toString()
            val address: String = soapObject.getProperty("Fulladdress").toString()
            val note: String = soapObject.getProperty("Notes").toString()
            val gender: String = soapObject.getProperty("Gender").toString()
            val nationality: String = soapObject.getProperty("Nationalty").toString()
            val country: String = soapObject.getProperty("Country").toString()
            val living: String = soapObject.getProperty("Living").toString()
            model = UserDataModel(
                id = id.toInt(),
                code = code.toInt(),
                password = password,
                email = email,
                genderId = genderId.toInt(),
                countryId = countryId.toInt(),
                picture = picture,
                status = status.toBoolean(),
                signUpDate = signUpDate,
                lastInteractionDate = lastInteractionDate,
                activationCode = activationCode,
                fullName = fullName,
                dateOfBirth = dateOfBirth,
                phone = phone,
                contact = contact,
                nationalityId = nationalityId.toInt(),
                livingId = livingId.toInt(),
                education = education,
                university = university,
                graduationYear = graduationYear,
                address = address,
                note = note,
                gender = gender,
                nationality = nationality, country = country, living = living
            )
        }
        return model
    }


}