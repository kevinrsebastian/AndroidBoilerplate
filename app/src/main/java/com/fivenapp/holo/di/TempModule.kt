package com.fivenapp.holo.di

import com.fivenapp.holo.temp.TempGreeter
import com.fivenapp.holo.temp.TempGreeterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class TempModule {

    @Provides
    fun provideTempGreeter(): TempGreeter {
        return TempGreeterImpl("World")
    }
}
