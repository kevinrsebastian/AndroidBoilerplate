package ph.teko.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ph.teko.app.util.rx.AsyncRxSchedulerUtils
import ph.teko.app.util.rx.RxSchedulerUtils

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UtilModule {

    @Binds
    abstract fun bindRxSchedulerUtils(rxSchedulerUtils: AsyncRxSchedulerUtils): RxSchedulerUtils
}

