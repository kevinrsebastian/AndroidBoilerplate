package ph.teko.app.testutil

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not

/**
 * A collection of simplified and less verbose [ViewAssertion]s
 */
object AssertionTestUtils {

    /**
     * Assert that the view has text that matches the parameter.
     * @param text the expected text on the view
     */
    fun assertHasText(text: String): ViewAssertion {
        return matches(withText(text))
    }

    /**
     * Assert that the view has a tag that matches the parameter.
     * @param tag the expected text on the view
     */
    fun assertHasTag(tag: String): ViewAssertion {
        return matches(withTagValue(`is`(tag as Any)))
    }

    // =================================================================================================================
    // Visibility Assertions
    // =================================================================================================================
    /**
     * Assert that the view is visible on the screen.
     * @see assertIsInvisible
     * @see assertIsGone
     */
    fun assertIsVisible(): ViewAssertion {
        return matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
    }

    /**
     * Assert that the view is not visible on the screen but retains its position
     * and occupies space.
     * @see assertIsVisible
     * @see assertIsGone
     */
    fun assertIsInvisible(): ViewAssertion {
        return matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE))
    }

    /**
     * Assert that the view is not visible and does not occupy space.
     * @see assertIsVisible
     * @see assertIsInvisible
     */
    fun assertIsGone(): ViewAssertion {
        return matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
    }
    // =================================================================================================================


    // =================================================================================================================
    // Display Assertions
    //   Assertions used to check if a view that is part of a fragment is displayed, regardless
    //   of the view visibility.
    // =================================================================================================================
    /**
     * Assert that the fragment holding the view is shown, and that view is displayed
     * regardless of visibility.
     * @see assertIsVisible
     * @see assertIsInvisible
     * @see assertIsGone
     */
    fun assertIsDisplayed(): ViewAssertion {
        return matches(isDisplayed())
    }

    /**
     * Assert that the fragment holding the view is not shown, and that view is not displayed
     * regardless of visibility.
     * @see assertIsVisible
     * @see assertIsInvisible
     * @see assertIsGone
     */
    fun assertIsNotDisplayed(): ViewAssertion {
        return matches(not(isDisplayed()))
    }
    // =================================================================================================================


    // =================================================================================================================
    // Enabled/Disabled Assertions
    // =================================================================================================================
    /** Assert that the view is enabled. */
    fun assertIsEnabled(): ViewAssertion {
        return matches(isEnabled())
    }

    /** Assert that the view is disabled. */
    fun assertIsDisabled(): ViewAssertion {
        return matches(not(isEnabled()))
    }
    // =================================================================================================================

    // =================================================================================================================
    // Activity Assertions
    // =================================================================================================================
    /** Assert that a called Activity has started. */
    fun assertActivityIsStarted(activityClassName: String) {
        Intents.intended(IntentMatchers.hasComponent(activityClassName))
    }
    // =================================================================================================================
}
