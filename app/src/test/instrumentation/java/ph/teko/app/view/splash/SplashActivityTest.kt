package ph.teko.app.view.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import ph.teko.app.test.base.BaseInstrumentationTest

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest : BaseInstrumentationTest() {

    private lateinit var scenario: ActivityScenario<SplashActivity>

    @Test
    fun launch() {
        scenario = launchActivity()
        Thread.sleep(3000)
    }
}
