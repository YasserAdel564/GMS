package com.gms.app.data.storage.remote.model.auth

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "living")
class LivingModel(
    @PrimaryKey()
    var id: Int,
    var name: String
) {
    override fun toString(): String {
        return name
    }
}

