package com.android.wpr.application.di.component

import android.app.Application
import com.android.wpr.application.AppApplication
import com.android.wpr.application.di.module.ActivityModule
import com.android.wpr.application.di.module.NetworkModule
import com.android.wpr.application.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ActivityModule::class,
        NetworkModule::class,
        ViewModelModule::class]
)
interface AppComponent : AndroidInjector<AppApplication> {
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Application): AppComponent

    }
}