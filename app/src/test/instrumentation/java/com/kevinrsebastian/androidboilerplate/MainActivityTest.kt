package com.kevinrsebastian.androidboilerplate

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertHasText
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsGone
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsVisible
import com.kevinrsebastian.androidboilerplate.testutil.ViewTestUtils.viewWithId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var tempGreeter: TempGreeter

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun launchDefault() {
        val expectedGreeting = "Hello Juan!"

        // Mock behaviour
        `when`(tempGreeter.greeting()).thenReturn(expectedGreeting)

        val scenario = launchActivity<MainActivity>()

        // Check initial loading state
        viewWithId(R.id.text_greeting).check(assertIsGone())
        viewWithId(R.id.progress_loading).check(assertIsVisible())

        Thread.sleep(1500)

        // Check that greeting is shown and loading is done
        viewWithId(R.id.text_greeting).check(assertIsVisible())
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))
        viewWithId(R.id.progress_loading).check(assertIsGone())

        // Verify behaviour
        verify(tempGreeter).greeting()
        verifyNoMoreInteractions(tempGreeter)

        scenario.close()
    }
}