package com.kevinrsebastian.androidboilerplate.di

import com.kevinrsebastian.androidboilerplate.util.rx.AsyncRxSchedulerUtils
import com.kevinrsebastian.androidboilerplate.util.rx.RxSchedulerUtils
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

