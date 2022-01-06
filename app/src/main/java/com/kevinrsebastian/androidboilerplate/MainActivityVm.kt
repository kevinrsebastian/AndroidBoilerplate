package com.kevinrsebastian.androidboilerplate

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors

class MainActivityVm : ViewModel() {

    private val greeting = MutableLiveData<String>()
    fun getGreeting() = greeting
    fun setGreeting(value: String) = greeting.postValue(value)

    private val isLoading = ObservableField(false)
    fun isLoading() = isLoading
    fun setLoading(value: Boolean) = isLoading.set(value)

    fun loadGreeting(greeting: String, loadingDelay: Long) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            setLoading(true)
            Thread.sleep(loadingDelay)

            setGreeting(greeting)
            setLoading(false)
        }
    }
}
