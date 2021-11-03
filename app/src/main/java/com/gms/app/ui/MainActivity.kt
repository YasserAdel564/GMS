package com.gms.app.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.databinding.ActivityMainBinding
import com.gms.app.utils.Injector
import com.gms.app.utils.MyContextWrapper
import com.gms.app.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private fun preferencesHelper() = Injector.getPreferenceHelper()
    private val viewModel: LauncherViewModel by viewModels()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun attachBaseContext(newBase: Context?) {
        if (preferencesHelper().language.isEmpty()) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, "en"))
        } else {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, preferencesHelper().language))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this, preferencesHelper().language)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillPreferencesHelper()
        viewModel
    }

    @SuppressLint("HardwareIds")
    private fun fillPreferencesHelper() {
        if (preferencesHelper.uid.isEmpty())
            preferencesHelper.uid = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    }

}