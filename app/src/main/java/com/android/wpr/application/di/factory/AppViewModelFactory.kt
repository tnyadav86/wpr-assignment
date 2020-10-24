package com.android.wpr.application.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AppViewModelFactory @Inject constructor(private val providerMap: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        var provider = providerMap[modelClass]
        if (provider == null) {
            for ((key, value) in providerMap) {
                if (modelClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }
        if (provider == null)
            throw IllegalArgumentException("Unknown model class $modelClass")

        return provider.get() as T
    }
}