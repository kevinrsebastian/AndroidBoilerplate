package ph.teko.app.user_model.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ph.teko.app.user_model.api.MockUserApi
import ph.teko.app.user_model.db.UserDao
import ph.teko.app.user_model.db.UserDb
import ph.teko.app.user_model.usecase.UserUseCase
import ph.teko.app.user_model.usecase.UserUseCaseImpl

/* Not set as internal because test hilt modules in this project module are not being detected during instrumentation.
 * The test module replacing this is moved to the app module. */
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
