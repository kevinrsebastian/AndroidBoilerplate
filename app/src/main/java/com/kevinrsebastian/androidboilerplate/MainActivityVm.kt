package com.kevinrsebastian.androidboilerplate

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevinrsebastian.androidboilerplate.model.data.User
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import com.kevinrsebastian.androidboilerplate.temp.TempService
import com.kevinrsebastian.androidboilerplate.util.rx.RxSchedulerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityVm @Inject constructor(
    private val rxSchedulerUtils: RxSchedulerUtils,
    private val tempService: TempService,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val greeting: LiveData<String> = MutableLiveData()
    fun setGreeting(value: String) = (greeting as MutableLiveData).postValue(value)

    val isLoading = ObservableField(false)
    fun setLoading(value: Boolean) = isLoading.set(value)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun loadGreeting(loadingDelay: Long) {
        tempService.getGreeting()
            .delay(loadingDelay, TimeUnit.MILLISECONDS)
            .compose(rxSchedulerUtils.singleAsyncSchedulerTransformer())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    setLoading(true)
                }
                override fun onSuccess(greeting: String) {
                    setGreeting(greeting)
                    setLoading(false)
                }
                override fun onError(e: Throwable) {
                    // No error handling yet
                    setLoading(false)
                }
            })
    }

    fun setGreetingSubject(subject: String) {
        tempService.setGreetingSubject(subject)
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

    fun greetApiUserWithId(id: String) {
        userUseCase.getUserFromApi(id)
            .flatMap { user ->
                userUseCase.addUserToDb(user)
                    .andThen(Single.fromCallable { user })
            }
            .compose(rxSchedulerUtils.singleAsyncSchedulerTransformer())
            .subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    setLoading(true)
                }
                override fun onSuccess(user: User) {
                    setGreeting("Hello ${user.firstName} ${user.lastName}!")
                    setLoading(false)
                }
                override fun onError(e: Throwable) {
                    setGreeting("No user detected")
                    setLoading(false)
                }
            })
    }

    fun greetDbUserWithId(id: String) {
        userUseCase.getUserFromDb(id)
            .compose(rxSchedulerUtils.singleAsyncSchedulerTransformer())
            .subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    setLoading(true)
                }
                override fun onSuccess(user: User) {
                    setGreeting("Hello ${user.firstName} ${user.lastName}!")
                    setLoading(false)
                }
                override fun onError(e: Throwable) {
                    setGreeting("No user detected")
                    setLoading(false)
                }
            })
    }
}
