package com.android.wpr.application.di.module



import com.android.wpr.application.network.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    companion object{
        const val BASE_URL="https://dl.dropboxusercontent.com/"
    }

    @Provides
    fun provideBaseUrl(): String{
        // We can configure here base url from Build(For different productFlavors) or String resource
        return  BASE_URL

    }
    @Provides
    fun provideRetrofitService(baseUrl:String): ApiService {
        // Whenever Dagger needs to provide an instance of type ApiService,
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
