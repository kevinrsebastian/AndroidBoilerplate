package ph.teko.app.ui_components.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ph.teko.app.ui_components.R
import ph.teko.app.ui_components.databinding.ActivityColorThemeBinding
import timber.log.Timber

class ColorThemeActivity : AppCompatActivity() {

    companion object {
        val TAG: String = ColorThemeActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityColorThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("$TAG Created")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_color_theme)
        binding.lifecycleOwner = this
    }
}
