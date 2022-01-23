package com.kevinrsebastian.androidboilerplate

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevinrsebastian.androidboilerplate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val vm: MainActivityVm by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = vm
        binding.onClickButton = View.OnClickListener {
            showGreetingWithDelay()
        }
    }

    override fun onStart() {
        super.onStart()
        showGreetingWithDelay()
    }

    private fun showGreetingWithDelay() {
        val loadingDelay = 1500L
        vm.loadGreeting(loadingDelay)
    }
}
