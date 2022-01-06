package com.kevinrsebastian.androidboilerplate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevinrsebastian.androidboilerplate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val vm: MainActivityVm by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = vm
        binding.lifecycleOwner = this
        showGreetingWithDelay()
    }

    private fun showGreetingWithDelay() {
        val greeting = "Hello World!"
        val loadingDelay = 1500L
        vm.loadGreeting(greeting, loadingDelay)
    }
}
