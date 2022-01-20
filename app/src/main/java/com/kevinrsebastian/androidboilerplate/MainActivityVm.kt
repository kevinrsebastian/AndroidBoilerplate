package com.kevinrsebastian.androidboilerplate

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevinrsebastian.androidboilerplate.temp.TempService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class MainActivityVm @Inject constructor(
    private val tempService: TempService
) : ViewModel() {

    private val greeting = MutableLiveData<String>()
    fun getGreeting() = greeting
    fun setGreeting(value: String) = greeting.postValue(value)

    private val isLoading = ObservableField(false)
    fun isLoading() = isLoading
    fun setLoading(value: Boolean) = isLoading.set(value)

    fun loadGreeting(loadingDelay: Long) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            setLoading(true)
            Thread.sleep(loadingDelay)
            setGreeting(tempService.getGreeting())
            setLoading(false)
        }
    }

    fun setGreetingSubject(subject: String) {
        tempService.setGreetingSubject(subject)
    }
}
