package ph.teko.app.testutil

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

object ViewTestUtils {

    // =================================================================================================================
    // View Matchers
    // =================================================================================================================
    fun viewWithId(@IdRes id: Int): ViewInteraction =
        onView(withId(id))

    fun viewWithText(text: String): ViewInteraction =
        onView(withText(text))
    // =================================================================================================================


    // =================================================================================================================
    // View Actions
    // =================================================================================================================
    fun clickViewWithId(@IdRes id: Int) {
        viewWithId(id).perform(click())
    }

    fun clickViewWithText(text: String) {
        viewWithText(text).perform(click())
    }
    // =================================================================================================================
}
