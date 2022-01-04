package com.kevinrsebastian.androidboilerplate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevinrsebastian.androidboilerplate.databinding.ActivityMainBinding
import java.util.concurrent.Executors

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
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            vm.setLoading(true)
            Thread.sleep(3000)

            vm.setGreeting("Hello World!")
            vm.setLoading(false)
        }
    }
}
