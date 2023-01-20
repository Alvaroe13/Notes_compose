package com.alvaro.notes_compose.di

import android.content.Context
import com.alvaro.notes_compose.common.utils.ResourceProvider
import com.alvaro.notes_compose.common.utils.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object{
        @Provides
        @Singleton
        fun providesResourceProvider(@ApplicationContext applicationContext: Context): ResourceProvider = ResourceProviderImpl(applicationContext)
    }

}