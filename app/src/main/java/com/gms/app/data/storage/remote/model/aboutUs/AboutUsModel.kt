package com.gms.app.data.storage.remote.model.aboutUs

import androidx.room.Entity
import androidx.room.PrimaryKey

class AboutUsModel(
    var id: Int,
    var aboutUs: String,
    var address: String,
    var phone: String,
    var fax: String,
    var email: String,
    var facebook: String,
    var twitter: String,
    var google: String,
    var linkedIn: String,
    var slogan: String,
    var map: String
)