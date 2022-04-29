package ph.teko.app.view

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import ph.teko.app.R
import ph.teko.app.test.base.BaseInstrumentationTest
import ph.teko.app.test.factory.UserFactory
import ph.teko.app.testutil.AssertionTestUtils.assertActivityIsStarted
import ph.teko.app.testutil.AssertionTestUtils.assertIsEnabled
import ph.teko.app.testutil.AssertionTestUtils.assertIsGone
import ph.teko.app.testutil.ViewTestUtils.clickViewWithId
import ph.teko.app.testutil.ViewTestUtils.viewWithId
import ph.teko.app.user_model.usecase.UserUseCase
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest : BaseInstrumentationTest() {

    @Inject
    lateinit var userUseCase: UserUseCase

    private lateinit var scenario: ActivityScenario<SplashActivity>

    @After
    override fun tearDown() {
        super.tearDown()
        scenario.close()
    }

    /**
     * Scenario: A logged-in user exists in cache
     */
    @Test
    fun launchWithLoggedInUser() {
        // Mock behaviour
        val user = UserFactory.user()
        `when`(userUseCase.getUserFromCache()).thenReturn(user)

        scenario = launchActivity()

        // No use checking for loading state since there is no delay to determine
        viewWithId(R.id.progress_loading).check(assertIsGone())
        viewWithId(R.id.btn_sign_in).check(assertIsEnabled())
        viewWithId(R.id.btn_join_as_as_technician).check(assertIsEnabled())

        verifyBehaviour()
    }

    /**
     * Scenario: There is no logged-in user in cache. Sign-In and Join as a Tech options are displayed. Pressing the
     *   Sign-In button will redirect the screen to SignInActivity.
     */
    @Test
    fun navigateToSignInActivity() {
        // Mock no logged-in user
        val user = null
        `when`(userUseCase.getUserFromCache()).thenReturn(user)

        scenario = launchActivity()

        // No use checking for loading state since there is no delay to determine
        viewWithId(R.id.progress_loading).check(assertIsGone())
        viewWithId(R.id.btn_sign_in).check(assertIsEnabled())
        viewWithId(R.id.btn_join_as_as_technician).check(assertIsEnabled())

        clickViewWithId(R.id.btn_sign_in)
        assertActivityIsStarted(SignInActivity::class.java.name)

        verifyBehaviour()
    }

    private fun verifyBehaviour() {
        verify(userUseCase).getUserFromCache()
        verifyNoMoreInteractions(userUseCase)
    }
}
