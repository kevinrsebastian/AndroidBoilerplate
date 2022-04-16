package ph.teko.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ph.teko.app.BuildConfig
import ph.teko.app.core.config.EnvConfig
import ph.teko.app.temp.TempGreeter
import ph.teko.app.temp.TempService
import ph.teko.app.temp.TempServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    fun provideEnvConfig(): EnvConfig {
        return EnvConfig(BuildConfig.TEKO_REST_API_URL)
    }

    @Provides
    fun provideTempService(tempGreeter: TempGreeter): TempService {
        return TempServiceImpl(tempGreeter)
    }
}
