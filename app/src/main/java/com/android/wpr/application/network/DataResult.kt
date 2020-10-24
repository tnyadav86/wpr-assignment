package com.android.wpr.application.network

sealed class DataResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Error(val exception: String) : DataResult<Nothing>()
    data class NetworkConnectionError(val exception: String) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is NetworkConnectionError -> "NetworkConnectionError[exception=$exception]"
        }
    }
}
