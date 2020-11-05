package com.android.wpr.application.di.module



import com.android.wpr.application.network.ApiService
import com.android.wpr.application.test.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class TestNetworkModule {

    companion object{
        const val BASE_URL="http://localhost:8080/"
    }

    @Provides
    fun provideBaseUrl(): String{
        // We can configure here base url from Build(For different productFlavors) or String resource
        return  BASE_URL

    }
    @Provides
    open fun httpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        clientBuilder.readTimeout(120, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(120, TimeUnit.SECONDS)
        return clientBuilder.build()
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
