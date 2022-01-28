package com.kevinrsebastian.androidboilerplate.di

import android.content.Context
import com.kevinrsebastian.androidboilerplate.api.MockApi
import com.kevinrsebastian.androidboilerplate.model.db.AndroidBoilerplateDb
import com.kevinrsebastian.androidboilerplate.model.db.UserDao
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ModelModule {

    // Should be in AppModule, but that module is currently installed in ViewModelComponent
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AndroidBoilerplateDb {
        return AndroidBoilerplateDb.create(context)
    }

    // Should be in AppModule, but that module is currently installed in ViewModelComponent
    @Provides
    fun provideUserDao(db: AndroidBoilerplateDb): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideUserUseCase(mockApi: MockApi, userDao: UserDao): UserUseCase {
        return UserUseCaseImpl(mockApi, userDao)
    }
}
