package com.android.wpr.application.di.module


import com.android.wpr.application.network.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    //https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json
    @Provides
    fun provideRetrofitService(): ApiService {
        // Whenever Dagger needs to provide an instance of type ApiService,
        return Retrofit.Builder()
            .baseUrl("https://dl.dropboxusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
