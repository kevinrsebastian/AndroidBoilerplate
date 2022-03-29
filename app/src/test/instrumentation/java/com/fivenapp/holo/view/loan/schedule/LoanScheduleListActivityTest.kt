package com.fivenapp.holo.view.loan.schedule

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fivenapp.holo.R
import com.fivenapp.holo.testutil.AssertionTestUtils.assertIsGone
import com.fivenapp.holo.testutil.AssertionTestUtils.assertIsVisible
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
internal class LoanScheduleListActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<LoanScheduleListActivity>

    @Before
    fun setUp() {
        Intents.init()
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun launchDefault() {
        // Launch Activity and assert initial state
        launchActivity()
    }

    private fun launchActivity() {
        scenario = androidx.test.core.app.launchActivity()
        assertLoading(true)

        // Advance the time to end loading
        val loadingDelay = 1000L
        Thread.sleep(loadingDelay)

        assertLoading(false)
        assertEmptyState()
    }

    private fun assertEmptyState() {
        viewWithId(R.id.container_empty_state).check(assertIsVisible())
    }

    private fun assertLoading(isLoading: Boolean) {
        if (isLoading) {
            viewWithId(R.id.progress_loading).check(assertIsVisible())
        } else {
            viewWithId(R.id.progress_loading).check(assertIsGone())
        }
    }
}
