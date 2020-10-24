package com.android.wpr.application.di.module

import androidx.lifecycle.ViewModelProvider
import com.android.wpr.application.di.factory.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: AppViewModelFactory
    ): ViewModelProvider.Factory




}