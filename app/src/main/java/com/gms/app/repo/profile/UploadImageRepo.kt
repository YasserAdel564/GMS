package com.gms.app.repo.profile

import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.addKeyPropertyString
import com.gms.app.utils.safeApiCall
import com.google.android.exoplayer2.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class UploadImageRepo @Inject constructor() {

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
        addKeyPropertyString(request, "fle", image)
        addKeyPropertyString(request, "FileName", imageName)
        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.UploadImage.value),
            envelope
        )
        response = envelope.response.toString() == "DONE"
        return response!!
    }

}