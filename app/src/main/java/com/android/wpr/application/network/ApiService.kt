@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection")

package com.android.wpr.application.network

import com.android.wpr.application.model.data.FeedResponseData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("s/2iodh4vg0eortkl/facts.json")
    fun fetchDetails(): Call<FeedResponseData>



}
