package com.android.wpr.application.di.component

import android.app.Application
import com.android.wpr.application.TestAppApplication
import com.android.wpr.application.di.module.ActivityModule
import com.android.wpr.application.di.module.TestNetworkModule
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
        TestNetworkModule::class,
        ViewModelModule::class]
)
interface TestAppComponent : AndroidInjector<TestAppApplication> {
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Application): TestAppComponent

    }
}