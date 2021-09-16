package com.gms.app.data.storage.remote.model.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genders")
class GenderModel(
    @PrimaryKey()
    var id: Int,
    var name: String
)
{
    override fun toString(): String {
        return name
    }
}

