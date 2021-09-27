package com.gms.app.data.storage.remote.model.home

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

class SliderModel(
    var id: Int,
    var headline: String,
    var paragraph: String,
    var text: String,
    var slice: String,
    var link: String
)
