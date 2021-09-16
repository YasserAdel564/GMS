package com.gms.app.utils


sealed class DataResource<out T> {
    data class Success<out T>(val value: T): DataResource<T>()
    data class Error(val type: Constants.ErrorType) : DataResource<Nothing>()
}