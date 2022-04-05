package ph.teko.app.test.base

import androidx.test.espresso.intent.Intents
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Base class for instrumentation tests. Handles the following:
 * - HiltAndroidRule and hilt injection (still need to add @HiltAndroidTest annotation to extending classes)
 * - Initialization and clean-up of espresso intents
 */
open class BaseInstrumentationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    open fun setUp() {
        Intents.init()
        hiltRule.inject()
    }

    @After
    open fun tearDown() {
        Intents.release()
    }
}
