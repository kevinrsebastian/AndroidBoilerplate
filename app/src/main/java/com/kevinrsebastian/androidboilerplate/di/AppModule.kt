package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.temp.TempService
import com.kevinrsebastian.androidboilerplate.temp.TempServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindTempService(tempService: TempServiceImpl): TempService
}
