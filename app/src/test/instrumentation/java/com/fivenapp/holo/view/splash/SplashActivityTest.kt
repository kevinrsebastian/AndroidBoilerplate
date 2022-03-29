package com.fivenapp.holo.view.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fivenapp.holo.R
import com.fivenapp.holo.testutil.AssertionTestUtils
import com.fivenapp.holo.testutil.AssertionTestUtils.assertActivityIsStarted
import com.fivenapp.holo.testutil.ViewTestUtils.viewWithId
import com.fivenapp.holo.view.loan.schedule.LoanScheduleListActivity
import com.fivenapp.test.base.BaseInstrumentationTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class SplashActivityTest : BaseInstrumentationTest() {

    private lateinit var scenario: ActivityScenario<SplashActivity>

    @After
    override fun tearDown() {
        super.tearDown()
        scenario.close()
    }

    @Test
    fun launchDefault() {
        // Launch Activity and assert initial state
        scenario = launchActivity<SplashActivity>()
        viewWithId(R.id.container_branding).check(AssertionTestUtils.assertIsVisible())
        assertLoading(true)

        // Advance the time to end loading
        val loadingDelay = 1000L
        Thread.sleep(loadingDelay)

        // Loading will no longer be seen since this will start a different activity
        // Assert that LoanScheduleListActivity has started
        assertActivityIsStarted(LoanScheduleListActivity::class.java.name)
    }

    private fun assertLoading(isLoading: Boolean) {
        if (isLoading) {
            viewWithId(R.id.progress_loading).check(AssertionTestUtils.assertIsVisible())
        } else {
            viewWithId(R.id.progress_loading).check(AssertionTestUtils.assertIsGone())
        }
    }
}
