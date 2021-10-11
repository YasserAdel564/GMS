package com.gms.app.repo.videos

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.data.storage.remote.model.programs.MyProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.data.storage.remote.model.programs.ProgrammePeriodModel
import com.gms.app.data.storage.remote.model.video.VideoModel
import com.gms.app.utils.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import javax.inject.Inject

class VideosRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private val videosList = ArrayList<VideoModel>()

    suspend fun getVideos(): DataResource<List<VideoModel>> {
        return safeApiCall { videoCall() }
    }

    private fun videoCall(): List<VideoModel> {
        val request =
            SoapObject(Constants.NameSpace, Constants.MethodNames.GetVideos.value)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        val androidHttpTransport = HttpTransportSE(Constants.BaseUrl)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        addKeyPropertyInt(request, "ID", preferencesHelper.userId.toInt())

        androidHttpTransport.call(
            (Constants.NameSpace + Constants.MethodNames.GetVideos.value),
            envelope
        )
        val resultsString = envelope.response as SoapObject
        val object1 = resultsString.getProperty(1) as SoapObject
        if (object1.propertyCount > 0) {
            videosList.clear()
            val tables = object1.getProperty(0) as SoapObject
            for (i in 0 until tables.propertyCount) {
                val soapObject = tables.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val title: String = soapObject.getProperty("Title").toString()
                val brief: String = soapObject.getProperty("Brief").toString()
                val details: String = soapObject.getProperty("details").toString()
                val img: String = soapObject.getProperty("picture").toString()
                val writer: String = soapObject.getProperty("Writer").toString()
                val link: String = soapObject.getProperty("Externallink").toString()
                val views: String = soapObject.getProperty("Views").toString()
                val reviews: String = soapObject.getProperty("Reviews").toString()
                val date: String = soapObject.getProperty("publishdate").toString()
                videosList.add(
                    (VideoModel(
                        id = id.toInt(),
                        title = title,
                        brief = brief,
                        details = details,
                        img = Constants.ImagesUrl + img,
                        writer = writer,
                        link = link,
                        views = views, reviews = reviews, date = date
                    ))
                )
            }
        }
        return videosList
    }

}