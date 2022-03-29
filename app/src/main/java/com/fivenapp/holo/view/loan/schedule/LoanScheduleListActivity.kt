package com.fivenapp.holo.view.loan.schedule

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fivenapp.holo.R
import com.fivenapp.holo.databinding.ActivityLoanScheduleListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoanScheduleListActivity : AppCompatActivity() {

    companion object {
        val TAG: String = LoanScheduleListActivity::class.java.simpleName
    }

    private val vm: LoanScheduleListActivityVm by viewModels()
    private lateinit var binding: ActivityLoanScheduleListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_loan_schedule_list)
        binding.lifecycleOwner = this
        binding.vm = vm
    }

    override fun onStart() {
        super.onStart()
        showLoadingWithDelay()
    }

    private fun showLoadingWithDelay() {
        val loadingDelay = 1000L
        vm.loadLoanSchedules(loadingDelay)
    }
}
