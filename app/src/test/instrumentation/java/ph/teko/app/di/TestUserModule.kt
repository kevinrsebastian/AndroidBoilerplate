package ph.teko.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import ph.teko.app.user_model.di.UserModule
import ph.teko.app.user_model.usecase.UserUseCase
import javax.inject.Singleton

/* Moved to the app module because test hilt modules in feature modules are not being detected unless they are
 * added as sourceSets. Adding those directories as sourceSets make the internal classes invisible to the modules. */
// TODO: Find a way to move this to user module
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserModule::class]
)
class TestUserModule {

    @Singleton
    @Provides
    fun provideUserUseCase(): UserUseCase {
        return Mockito.mock(UserUseCase::class.java)
    }
}
