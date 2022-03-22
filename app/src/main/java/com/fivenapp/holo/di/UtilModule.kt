package com.fivenapp.holo.di

import com.fivenapp.holo.util.rx.AsyncRxSchedulerUtils
import com.fivenapp.holo.util.rx.RxSchedulerUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UtilModule {

    @Binds
    abstract fun bindRxSchedulerUtils(rxSchedulerUtils: AsyncRxSchedulerUtils): RxSchedulerUtils
}

