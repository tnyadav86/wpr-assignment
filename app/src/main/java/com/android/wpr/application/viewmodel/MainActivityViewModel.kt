package com.android.wpr.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.wpr.application.model.DataRepository
import com.android.wpr.application.model.NetworkConnectionLiveData
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.DataResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val dataRepository: DataRepository,
                                                mApplication:Application) : AndroidViewModel(mApplication) {

    private val _networkConnectionLiveData: NetworkConnectionLiveData = NetworkConnectionLiveData(mApplication)
    val networkConnectionLiveData: NetworkConnectionLiveData
        get() = _networkConnectionLiveData

    private val _feedResponseLiveData = dataRepository.dataResult
    val feedResponseLiveData: LiveData<DataResult<FeedResponseData>>
        get() = _feedResponseLiveData

     fun fetchData() {
         //creating coroutine on ui thread
        viewModelScope.launch {
            dataRepository.fetchData(networkConnectionLiveData.value?:false)
        }
    }
}
