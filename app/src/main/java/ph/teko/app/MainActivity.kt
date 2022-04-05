package ph.teko.app

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import ph.teko.app.databinding.ActivityMainBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private val vm: MainActivityVm by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = vm
        binding.loadFromApi = View.OnClickListener {
            vm.greetApiUserWithId(binding.editApiUserId.text.toString())
        }
        binding.loadFromDb = View.OnClickListener {
            vm.greetDbUserWithId(binding.editDbUserId.text.toString())
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
