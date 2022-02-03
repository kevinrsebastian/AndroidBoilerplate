package com.kevinrsebastian.user.di

import android.content.Context
import com.kevinrsebastian.user.model.api.MockUserApi
import com.kevinrsebastian.user.model.db.UserDao
import com.kevinrsebastian.user.model.db.UserDb
import com.kevinrsebastian.user.model.usecase.UserUseCase
import com.kevinrsebastian.user.model.usecase.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/*
 * Not set as internal because test hilt modules in this feature module are not being detected during instrumentation.
 * The test module replacing this is moved to the app module.
 */
@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    internal fun provideDatabase(@ApplicationContext context: Context): UserDb {
        return UserDb.create(context)
    }

    @Provides
    internal fun provideUserDao(db: UserDb): UserDao {
        return db.userDao()
    }

    @Provides
    internal fun provideUserUseCase(mockUserApi: MockUserApi, userDao: UserDao): UserUseCase {
        return UserUseCaseImpl(mockUserApi, userDao)
    }
}
