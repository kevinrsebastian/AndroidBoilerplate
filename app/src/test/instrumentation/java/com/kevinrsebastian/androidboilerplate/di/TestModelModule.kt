package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ModelModule::class]
)
class TestModelModule {

    @Singleton
    @Provides
    fun provideUserUseCase(): UserUseCase {
        return Mockito.mock(UserUseCase::class.java)
    }
}
