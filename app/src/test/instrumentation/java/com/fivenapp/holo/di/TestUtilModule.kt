package com.fivenapp.holo.di

import com.fivenapp.holo.util.rx.RxSchedulerUtils
import com.fivenapp.holo.util.rx.SyncRxSchedulerUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
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
