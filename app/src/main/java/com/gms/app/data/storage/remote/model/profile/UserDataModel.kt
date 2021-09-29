package com.gms.app.data.storage.remote.model.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

class UserDataModel(
    var id: Int,
    var code: Int,
    var password: String,
    var email: String,
    var genderId: Int,
    var countryId: Int,
    var picture: String,
    var status: Boolean,
    var signUpDate: String,
    var lastInteractionDate: String,
    var activationCode: String,
    var fullName: String,
    var dateOfBirth: String,
    var phone: String,
    var contact: String,
    var nationalityId: Int,
    var livingId: Int,
    var education: String,
    var university: String,
    var graduationYear: String,
    var address: String,
    var note: String,
    var gender: String,
    var nationality: String,
    var country: String,
    var living: String
)
