package com.android.wpr.application.model

import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.DataResult
import javax.inject.Inject

class DataRepository @Inject constructor(private val remoteDataSource: RemoteDataSource){

    suspend fun fetchData(): DataResult<FeedResponseData> {
       return remoteDataSource.fetchData()
    }

}
