package com.kevinrsebastian.androidboilerplate

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityVm : ViewModel() {

    private val greeting = MutableLiveData<String>()
    fun getGreeting() = greeting
    fun setGreeting(value: String) = greeting.postValue(value)

    private val isLoading = ObservableField(false)
    fun isLoading() = isLoading
    fun setLoading(value: Boolean) = isLoading.set(value)
}
