package ph.teko.app.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import ph.teko.app.core.di.CoreModule
import javax.inject.Singleton

/* Moved to the app module because test hilt modules in other project modules are not being detected unless they are
 * added as sourceSets. Adding those directories as sourceSets make the internal classes invisible to the modules. */
// TODO: Find a way to move this to user-model module
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoreModule::class]
)
class TestCoreModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return Mockito.mock(SharedPreferences::class.java)
    }
}
