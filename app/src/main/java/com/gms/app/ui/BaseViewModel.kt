package com.gms.app.ui

import androidx.lifecycle.ViewModel
import com.gms.app.utils.Injector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel: ViewModel() {

    val dispatcherProvider = Injector.getCoroutinesDispatcherProvider()
    private val job = Job()
    val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}