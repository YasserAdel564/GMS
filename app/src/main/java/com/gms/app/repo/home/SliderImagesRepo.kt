package com.gms.app.repo.home

import android.util.Log
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.local.db.AppDao
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.home.SliderModel
import com.gms.app.utils.Constants
import com.gms.app.utils.DataResource
import com.gms.app.utils.safeApiCall
import com.gms.app.utils.soapRequestBuilder
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class SliderImagesRepo @Inject
constructor(
    private val preferencesHelper: PreferencesHelper
) {

    private val sliderList = ArrayList<SliderModel>()

    suspend fun getSliderImages(): DataResource<List<SliderModel>> {
        return safeApiCall { sliderCall() }
    }

    private fun sliderCall(): List<SliderModel> {
        sliderList.clear()
        val table =
            soapRequestBuilder(
                Constants.MethodNames.GetSliderImages.value,
                preferencesHelper.language
            )
        if (table != null) {
            for (i in 0 until table.propertyCount) {
                val soapObject = table.getProperty(i) as SoapObject
                val id: String = soapObject.getProperty("ID").toString()
                val headline: String = soapObject.getProperty("headline").toString()
                val paragraph: String = soapObject.getProperty("Pragraph").toString()
                val text: String = soapObject.getProperty("Text").toString()
                val slice: String = soapObject.getProperty("Slices").toString()
                val link: String = soapObject.getProperty("Link").toString()
                sliderList.add(
                    (SliderModel(
                        id = id.toInt(),
                        headline = headline,
                        paragraph = paragraph,
                        text = text,
                        slice = Constants.ImagesUrl + slice ,
                        link = link,
                    ))
                )
            }

        }
        return sliderList
    }

}