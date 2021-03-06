package com.gms.app.data.storage.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext val context: Context){

    companion object {
        private lateinit var preference: SharedPreferences
        const val SHARED_NAME = "GMS"
        private const val UID = "UID"
        private const val LANGUAGE = "LANGUAGE"
        private const val IsLOGIN = "IsLOGIN"
        private const val UserId = "UserId"
    }

    init {
        preference = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    var uid :String
        get() = preference.getString(UID, "").toString()
        set(value) = preference.edit().putString(UID, value).apply()

    var language :String
        get() = preference.getString(LANGUAGE, "EN").toString()
        set(value) = preference.edit().putString(LANGUAGE, value).apply()

    var isLogin :Boolean
        get() = preference.getBoolean(IsLOGIN, false)
        set(value) = preference.edit().putBoolean(IsLOGIN, value).apply()

    var userId :String
        get() = preference.getString(UserId, null).toString()
        set(value) = preference.edit().putString(UserId, value).apply()

}