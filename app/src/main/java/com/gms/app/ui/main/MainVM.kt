package com.gms.app.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import com.gms.app.ui.BaseViewModel

class MainVM  @ViewModelInject
constructor() : BaseViewModel() {
    var isOpened = false
}