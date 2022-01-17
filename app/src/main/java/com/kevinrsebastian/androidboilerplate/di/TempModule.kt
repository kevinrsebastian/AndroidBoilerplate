package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import com.kevinrsebastian.androidboilerplate.temp.TempGreeterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class TempModule {

    @Provides
    fun provideTempGreeter(): TempGreeter {
        return TempGreeterImpl("World")
    }
}
