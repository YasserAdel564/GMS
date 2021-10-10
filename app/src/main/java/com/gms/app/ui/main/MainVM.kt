package com.gms.app.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.gms.app.ui.BaseViewModel

class MainVM @ViewModelInject
constructor() : BaseViewModel() {
    var isOpened = false

    val programmeId= MutableLiveData<Int>()
    fun setProgrammeId(id: Int) {
        programmeId.value = id
    }

}