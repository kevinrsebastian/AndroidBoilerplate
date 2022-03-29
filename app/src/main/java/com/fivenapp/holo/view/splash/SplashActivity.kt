package com.fivenapp.holo.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fivenapp.holo.R
import com.fivenapp.holo.databinding.ActivitySplashBinding
import com.fivenapp.holo.view.loan.schedule.LoanScheduleListActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        val TAG: String = SplashActivity::class.java.simpleName
    }

    private val vm: SplashActivityVm by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.vm = vm

        showLoadingWithDelay()
    }

    private fun navigateToLoanAmortizationActivity() {
        startActivity(Intent(this, LoanScheduleListActivity::class.java))
        finish()
    }

    private fun showLoadingWithDelay() {
        val loadingDelay = 1000L
        vm.loadDelay(loadingDelay) { navigateToLoanAmortizationActivity() }
    }
}
