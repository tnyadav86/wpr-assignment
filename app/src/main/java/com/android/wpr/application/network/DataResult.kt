package com.android.wpr.application.network

sealed class DataResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Error(val errorCode: FeedDataErrorCode) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$errorCode]"
        }
    }
}
