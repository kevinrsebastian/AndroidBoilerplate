package ph.teko.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ph.teko.app.temp.TempGreeter
import ph.teko.app.temp.TempGreeterImpl

@Module
@InstallIn(SingletonComponent::class)
internal class TempModule {

    @Provides
    fun provideTempGreeter(): TempGreeter {
        return TempGreeterImpl("World")
    }
}
