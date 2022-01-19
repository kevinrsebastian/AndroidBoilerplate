package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TempModule::class]
)
class TestTempModule {

    @Singleton
    @Provides
    fun provideTempGreeter(): TempGreeter {
        return Mockito.mock(TempGreeter::class.java)
    }
}
