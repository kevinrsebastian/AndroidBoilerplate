package ph.teko.app.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import ph.teko.app.R
import ph.teko.app.databinding.ActivitySplashBinding
import ph.teko.app.vm.SplashActivityVm
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
        setUpBinding()
    }

    override fun onStart() {
        super.onStart()
        vm.checkForLoggedInUser(navigateToHomeScreen = {
            // TODO: Navigate to home screen
        })
    }

    // Should be called inside the Activity.onCreate function
    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
    }
}
