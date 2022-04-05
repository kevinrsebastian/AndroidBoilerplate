package ph.teko.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import ph.teko.app.temp.TempGreeter
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
