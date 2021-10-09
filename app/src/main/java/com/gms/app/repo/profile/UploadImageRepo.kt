package com.gms.app.repo.profile

import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.google.android.exoplayer2.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class UploadImageRepo @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) {

    private var response: Boolean? = null

    suspend fun uploadImage(image: String, imageName: String): DataResource<Boolean> {
        return safeApiCall { upload(image, imageName) }
    }

    private fun upload(image: String, imageName: String): Boolean {
        val request = SoapObject(Constants.NameSpace, Constants.MethodNames.UploadImage.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        val imageByte = PropertyInfo()
        imageByte.setName("fle")
        imageByte.value = image
        imageByte.setType(String::class.java)
        request.addProperty(imageByte)

        val name = PropertyInfo()
        name.setName("FileName")
        name.value = imageName
        name.setType(String::class.java)
        request.addProperty(name)

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.UploadImage.value),
            envelope
        )

        val resultsString = envelope.response as SoapObject
        Log.e("resultsString", resultsString.toString())

        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            val tables = object1.getProperty(0) as SoapObject
            val soapObject = tables.getProperty(0) as SoapObject
            val result: String = soapObject.getProperty("UploadProfileImageResult").toString()
            Log.e("response", tables.toString())
            response = result == "DONE"

        }
        return response!!
    }

}