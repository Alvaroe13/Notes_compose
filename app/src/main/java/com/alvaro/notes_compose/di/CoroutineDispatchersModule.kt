package com.alvaro.notes_compose.di

import com.alvaro.core.util.DispatcherProvider
import com.alvaro.core.util.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider() : DispatcherProvider {
        return DispatcherProviderImpl()
    }

}