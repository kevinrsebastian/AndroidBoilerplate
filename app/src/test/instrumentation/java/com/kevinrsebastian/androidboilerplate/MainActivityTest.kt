package com.kevinrsebastian.androidboilerplate

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertHasText
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsGone
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsVisible
import com.kevinrsebastian.androidboilerplate.testutil.ViewTestUtils.viewWithId
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MainActivityTest {

    @Test
    fun launch() {
        val scenario = launchActivity<MainActivity>()

        // Check initial loading state
        viewWithId(R.id.text_greeting).check(assertIsGone())
        viewWithId(R.id.progress_loading).check(assertIsVisible())

        // The delay
        Thread.sleep(1500)

        // Check that greeting is shown and loading is done
        viewWithId(R.id.text_greeting).check(assertIsVisible())
        viewWithId(R.id.text_greeting).check(assertHasText("Hello World!"))
        viewWithId(R.id.progress_loading).check(assertIsGone())

        scenario.close()
    }
}
