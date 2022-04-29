package ph.teko.app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import ph.teko.app.R
import ph.teko.app.databinding.ActivitySignInBinding
import timber.log.Timber

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    companion object {
        val TAG: String = SignInActivity::class.java.simpleName
    }

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")
        setUpBinding()
    }

    // Should be called inside the Activity.onCreate function
    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.lifecycleOwner = this
    }
}
