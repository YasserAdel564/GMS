package com.gms.app.utils

import com.gms.app.MyApp
import com.gms.app.data.storage.local.PreferencesHelper
import kotlinx.coroutines.Dispatchers

object Injector {

    private val coroutinesDispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    private fun getApplicationContext() = MyApp.instance
    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())
    fun getCoroutinesDispatcherProvider() = coroutinesDispatcherProvider
}