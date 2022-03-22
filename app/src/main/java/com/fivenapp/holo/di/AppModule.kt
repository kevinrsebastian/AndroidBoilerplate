package com.fivenapp.holo.di

import com.fivenapp.holo.temp.TempService
import com.fivenapp.holo.temp.TempServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {

    @Binds
    abstract fun bindTempService(tempService: TempServiceImpl): TempService
}
