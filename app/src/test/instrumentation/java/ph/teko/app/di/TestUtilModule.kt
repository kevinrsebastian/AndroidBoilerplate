package ph.teko.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ph.teko.app.util.rx.RxSchedulerUtils
import ph.teko.app.util.rx.SyncRxSchedulerUtils
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UtilModule::class]
)
internal abstract class TestUtilModule {

    @Singleton
    @Binds
    abstract fun bindRxSchedulerUtils(rxSchedulerUtils: SyncRxSchedulerUtils): RxSchedulerUtils
}
