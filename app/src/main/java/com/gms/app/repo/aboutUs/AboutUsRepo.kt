package com.gms.app.repo.aboutUs


import com.blankj.utilcode.util.DeviceUtils
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.aboutUs.AboutUsModel
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


class AboutUsRepo @Inject
constructor() {

    private lateinit var model: AboutUsModel

    suspend fun getAboutUsData(): DataResource<AboutUsModel> {
        return safeApiCall { aboutUs() }
    }

    private fun aboutUs(): AboutUsModel {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.GetAboutUsData.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)
        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetAboutUsData.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val id: String = soapObject.getProperty("ID").toString()
            val aboutUs: String = soapObject.getProperty("AboutUs").toString()
            val address: String = soapObject.getProperty("Address").toString()
            val phone: String = soapObject.getProperty("Telephone").toString()
            val fax: String = soapObject.getProperty("Fax").toString()
            val email: String = soapObject.getProperty("Email").toString()
            val facebook: String = soapObject.getProperty("Facebook").toString()
            val twitter: String = soapObject.getProperty("Twitter").toString()
            val google: String = soapObject.getProperty("Google").toString()
            val linkedIn: String = soapObject.getProperty("Linkedin").toString()
            val slogan: String = soapObject.getProperty("Slogan").toString()
            val map: String = soapObject.getProperty("Map").toString()
            model = AboutUsModel(
                id = id.toInt(),
                aboutUs = aboutUs,
                address = address,
                phone = phone,
                fax = fax,
                email = email,
                facebook = facebook,
                twitter = twitter,
                google = google,
                linkedIn = linkedIn,
                slogan = slogan,
                map = map
            )
        }
        return model
    }


}