package ph.teko.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ph.teko.app.temp.TempService
import ph.teko.app.temp.TempServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {

    @Binds
    abstract fun bindTempService(tempService: TempServiceImpl): TempService
}
