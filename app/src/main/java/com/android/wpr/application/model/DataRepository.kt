package com.android.wpr.application.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.DataResult
import com.android.wpr.application.network.FeedDataErrorCode
import com.android.wpr.application.util.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepository @Inject constructor(private val remoteDataSource: RemoteDataSource,private val dispatchers: DispatcherProvider) {
    private val _dataResult = MutableLiveData<DataResult<FeedResponseData>>()
    val dataResult: LiveData<DataResult<FeedResponseData>>
        get() = _dataResult

    suspend fun fetchData(isInternetConnected: Boolean) {

        if (isInternetConnected) {
            _dataResult.postValue(DataResult.Loading())
            // Moving the execution of the fetch data to the I/O dispatcher
            _dataResult.postValue(withContext(dispatchers.io()) { remoteDataSource.fetchData() })
        } else {
            _dataResult.postValue(DataResult.Error(FeedDataErrorCode.INTERNET_CONNECTION_ERROR))
        }


    }

}
