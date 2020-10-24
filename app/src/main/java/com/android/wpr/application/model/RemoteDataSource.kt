package com.android.wpr.application.model

import android.util.Log
import com.android.wpr.application.model.data.FeedResponseData
import com.android.wpr.application.network.ApiService
import com.android.wpr.application.network.DataResult
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchData(): DataResult<FeedResponseData> {
        Log.e("wp", "Api request started")
        try {
            val response = apiService.fetchDetails().execute()
            if (response.isSuccessful) {
                response.body()?.let { feedResponseData ->
                    return DataResult.Success(feedResponseData)
                } ?: return DataResult.Error("No data found")
            } else {
                return DataResult.Error("Unable to get data. Please try again later")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DataResult.Error("Unable to get data. Please try again later")
        }

    }
}
