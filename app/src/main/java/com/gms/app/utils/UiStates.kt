package com.gms.app.utils

sealed class UiStates {
    object Loading : UiStates()
    object Success : UiStates()
    object LastPage : UiStates()
    object Error : UiStates()
    object NotFound : UiStates()
    object NoConnection : UiStates()
    object Empty : UiStates()
}