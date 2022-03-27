package com.fivenapp.holo.view.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fivenapp.holo.R
import com.fivenapp.holo.testutil.AssertionTestUtils
import com.fivenapp.holo.testutil.ViewTestUtils.viewWithId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class SplashActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<SplashActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun launchDefault() {
        launchActivity()
        assertLoading(true)
        val loadingDelay = 1000L
        Thread.sleep(loadingDelay)
        assertLoading(false)
    }

    private fun launchActivity() {
        scenario = launchActivity<SplashActivity>()
        viewWithId(R.id.container_branding).check(AssertionTestUtils.assertIsVisible())
    }

    private fun assertLoading(isLoading: Boolean) {
        if (isLoading) {
            viewWithId(R.id.progress_loading).check(AssertionTestUtils.assertIsVisible())
        } else {
            viewWithId(R.id.progress_loading).check(AssertionTestUtils.assertIsGone())
        }
    }
}
