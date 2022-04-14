package ph.teko.app.view.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import ph.teko.app.R
import ph.teko.app.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
internal class SplashActivity : AppCompatActivity() {
    companion object {
        val TAG: String = SplashActivity::class.java.simpleName
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }
}
