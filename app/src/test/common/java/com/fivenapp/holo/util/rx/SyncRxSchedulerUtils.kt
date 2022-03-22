package com.fivenapp.holo.util.rx

import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import javax.inject.Inject

/* Synchronous RxSchedulerUtils for testing purposes */
internal open class SyncRxSchedulerUtils @Inject constructor() : RxSchedulerUtils {

    override fun completableAsyncSchedulerTransformer(): CompletableTransformer =
        CompletableTransformer { upstream -> upstream }

    override fun <T> observableAsyncSchedulerTransformer(): ObservableTransformer<T, T> =
        ObservableTransformer { upstream -> upstream }

    override fun <T> singleAsyncSchedulerTransformer(): SingleTransformer<T, T> =
        SingleTransformer { upstream -> upstream }
}
