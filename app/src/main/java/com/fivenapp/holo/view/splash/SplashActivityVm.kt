package com.fivenapp.holo.view.splash

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.fivenapp.holo.util.rx.RxSchedulerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SplashActivityVm @Inject constructor(
    private val rxSchedulerUtils: RxSchedulerUtils,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val isLoading = ObservableField(false)
    fun setLoading(value: Boolean) = isLoading.set(value)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun loadDelay(loadingDelay: Long) {
        Completable.complete()
            .delay(loadingDelay, TimeUnit.MILLISECONDS)
            .compose(rxSchedulerUtils.completableAsyncSchedulerTransformer())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    setLoading(true)
                }
                override fun onComplete() {
                    setLoading(false)
                }
                override fun onError(e: Throwable) {
                    // No error handling yet
                    setLoading(false)
                }
            })
    }
}
