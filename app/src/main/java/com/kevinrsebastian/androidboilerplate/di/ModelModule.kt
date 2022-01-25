package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.api.MockApi
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ModelModule {

    @Provides
    fun provideUserUseCase(mockApi: MockApi): UserUseCase {
        return UserUseCaseImpl(mockApi)
    }
}
