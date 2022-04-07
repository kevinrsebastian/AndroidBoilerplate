package ph.teko.app.ui_components.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ph.teko.app.ui_components.R
import ph.teko.app.ui_components.databinding.ActivityThemeColorsBinding
import timber.log.Timber

// No Tests
class ThemeColorsActivity : AppCompatActivity() {
    companion object {
        val TAG: String = ThemeColorsActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityThemeColorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme_colors)
        binding.lifecycleOwner = this
    }
}
