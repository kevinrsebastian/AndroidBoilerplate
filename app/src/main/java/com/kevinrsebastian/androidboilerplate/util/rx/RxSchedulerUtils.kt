package com.kevinrsebastian.androidboilerplate.util.rx

import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer

/**
 * Transformer for any RxJava stream so it subscribes to a separate thread and is
 * observed on the android UI thread.
 */
interface RxSchedulerUtils {

    fun completableAsyncSchedulerTransformer(): CompletableTransformer

    fun <T> observableAsyncSchedulerTransformer(): ObservableTransformer<T, T>

    fun <T> singleAsyncSchedulerTransformer(): SingleTransformer<T, T>
}
