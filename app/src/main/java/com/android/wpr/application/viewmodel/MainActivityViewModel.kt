package com.android.wpr.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.wpr.application.model.DataRepository
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

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
            _feedResponseLiveData.postValue(dataRepository.fetchData())
            _loadingStatus.postValue(false)
        }
    }
}
