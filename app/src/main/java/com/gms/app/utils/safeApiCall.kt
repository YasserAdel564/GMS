package com.gms.app.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(call: suspend () -> T): DataResource<T> {
    return withContext(Dispatchers.IO) {
        try {
            DataResource.Success(call.invoke())
        } catch (throwable: Throwable) {
            Log.e("safeCall error message", throwable.toString())
            when (throwable) {
                is SocketTimeoutException -> DataResource.Error(Constants.ErrorType.TIMEOUT)
                is IOException -> DataResource.Error(Constants.ErrorType.NETWORK)
                else -> DataResource.Error(Constants.ErrorType.UNKNOWN)
            }
        }
    }
}
