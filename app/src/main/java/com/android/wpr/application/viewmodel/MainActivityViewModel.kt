package com.android.wpr.application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.wpr.application.model.DataRepository
import com.android.wpr.application.model.NetworkConnectionLiveData
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.DataResult
import com.android.wpr.application.network.FeedDataErrorCode
import com.android.wpr.application.util.isNetworkConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val dataRepository: DataRepository,private val mApplication:Application) : AndroidViewModel(mApplication) {

    private val _networkConnectionLiveData: NetworkConnectionLiveData = NetworkConnectionLiveData(mApplication)
    val networkConnectionLiveData: NetworkConnectionLiveData
        get() = _networkConnectionLiveData

    private val _feedResponseLiveData = MutableLiveData<DataResult<FeedResponseData>>()
    val feedResponseLiveData: LiveData<DataResult<FeedResponseData>>
        get() = _feedResponseLiveData


    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    init {
        _loadingStatus.value=false
        fetchData(true)
    }


    fun refreshData(showLoader :Boolean) {
        fetchData(showLoader)
    }
    // no need to show progressbar if refreshData being called from pull to refresh
    private fun fetchData(showLoader :Boolean?=true) {

        _loadingStatus.value = showLoader
        viewModelScope.launch(Dispatchers.IO) {
            networkConnectionLiveData.value?.let {isConnected->
                Log.e("wp vm isConnected",isConnected.toString())
                if (!isConnected){
                    _feedResponseLiveData.postValue(DataResult.Error(FeedDataErrorCode.INTERNET_CONNECTION_ERROR))
                    _loadingStatus.postValue(false)
                    return@launch
                }
            }?: kotlin.run{ // NetworkConnectionLiveData take time to update its value so cross check for network not connected
                if(!mApplication.isNetworkConnected){
                    _feedResponseLiveData.postValue(DataResult.Error(FeedDataErrorCode.INTERNET_CONNECTION_ERROR))
                    _loadingStatus.postValue(false)
                    return@launch
                }
                Log.e("wp viewmodel","networkConnectionLiveData.value is null")

            }

            _feedResponseLiveData.postValue(dataRepository.fetchData())
            _loadingStatus.postValue(false)
        }
    }
}
