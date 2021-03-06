package com.gms.app.utils


object Constants {
    const val BaseUrl = "http://services.gmsformedical.net/Service.asmx"
    const val ImagesUrl = "http://gmsformedical.com/images/"
    const val NameSpace = "http://tempuri.org/"
    const val RC_PERMISSION_STORAGE_CAMERA = 100
    const val RC_CAPTURE_IMAGE = 1000
    const val RC_IMAGES = 112

    enum class Language(val value: String) {
        ARABIC("AR"),
        ENGLISH("EN")
    }

    enum class MethodNames(val value: String) {
        GetCountries("GetCountry"),
        GetGender("GetGender"),
        GetLiving("GetLiving"),
        GetNationality("GetNationalty"),
        SignUp("ADD_New_Customers"),
        Login("GetLogin"),
        GetSliderImages("GetSlices"),
        GetAllPrograms("DetDiplomas"),
        GetUserData("GetCustomers"),
        GetAboutUsData("GetAllData"),
        GetProgrammeDetails("GetDiplomaDetails"),
        UploadImage("UploadProfileImage"),
        GetProgrammePeriods("GetPeriods"),
        AddCustomerProgramme("ADD_Customers_Diplomas"),
        GetCustomerProgramme("GetCustomers_Diplomas"),
        GetVideos("GetUserVideos"),
        ContactUs("ADD_New_Callers"),
        UpdateProfile("Update_New_Customers")
    }

    enum class ErrorType {
        NETWORK,
        TIMEOUT,
        UNKNOWN
    }

    enum class Exceptions(val value: String) {
        Null("anyType{}")
    }
}