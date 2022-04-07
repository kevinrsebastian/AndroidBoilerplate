package ph.teko.app.ui_components.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ph.teko.app.ui_components.R
import ph.teko.app.ui_components.databinding.ActivityThemeComponentsBinding
import timber.log.Timber
import java.util.concurrent.TimeUnit

// No Tests
class ThemeComponentsActivity : AppCompatActivity() {
    companion object {
        val TAG: String = ThemeComponentsActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityThemeComponentsBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme_components)
        binding.lifecycleOwner = this

        // Disable button for 1000ms after clicking
        val buttonClickListener = View.OnClickListener { disableButtonWithDelay(it) }
        binding.btnDefault.setOnClickListener(buttonClickListener)
        binding.btnTonal.setOnClickListener(buttonClickListener)
        binding.btnAccent.setOnClickListener(buttonClickListener)
        binding.btnOutlined.setOnClickListener(buttonClickListener)
        binding.btnOutlinedMono.setOnClickListener(buttonClickListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun disableButtonWithDelay(view: View) {
        // TODO: Refactor to use RxSchedulers
        Completable.complete()
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    view.isEnabled = false
                }
                override fun onComplete() {
                    view.isEnabled = true
                }
                override fun onError(e: Throwable) {
                    view.isEnabled = true
                }
            })
    }
}
