package com.gms.app.utils


object Constants {
    const val BaseUrl = "http://services.gmsformedical.net/Service.asmx"
    const val NameSpace = "http://tempuri.org/"

    enum class Language(val value: String) {
        ARABIC("AR"),
        ENGLISH("EN")
    }

    enum class MethodNames(val value: String) {
        GetCountries("GetCountry"),
        GetGender("GetGender"),
        GetLiving("GetLiving"),
        GetNationality("GetNationalty"),
        SignUp("ADD_New_Customers")
    }

    enum class ErrorType {
        NETWORK,
        TIMEOUT,
        UNKNOWN
    }

}