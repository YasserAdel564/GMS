package com.gms.app.data.storage.remote.model.home

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

class SliderModel(
    var name: String,
    var bg: String,
    var media: String,
    var title: String,
    var text: String,
    var btn: String,
    var link: String
)
