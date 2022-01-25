package com.kevinrsebastian.androidboilerplate

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinrsebastian.androidboilerplate.model.data.User
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertHasText
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsGone
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsVisible
import com.kevinrsebastian.androidboilerplate.testutil.ViewTestUtils.viewWithId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val factory = DataFactory()

    lateinit var scenario: ActivityScenario<MainActivity>

    @Inject
    lateinit var tempGreeter: TempGreeter

    @Inject
    lateinit var userUseCase: UserUseCase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun launchDefault() {
        launchActivity()

        // Verify behaviour
        verify(tempGreeter).greeting()
        verifyNoMoreInteractions(tempGreeter)

        scenario.close()
    }

    @Test
    fun searchExistingUser() {
        val user = User(factory.getNumberBetween(1, 50).toString(), factory.firstName, factory.lastName)
        val expectedGreeting = "Hello ${user.firstName} ${user.lastName}!"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromApi(user.id))
            .thenReturn(Single.just(user).delay(loadingDelay, MILLISECONDS))

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_user_id).perform(typeText(user.id))
        viewWithId(R.id.btn_search).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertGreetingIsShown(expectedGreeting)

        // Verify behaviour
        verify(tempGreeter).greeting()
        verifyNoMoreInteractions(tempGreeter)

        scenario.close()
    }

    @Test
    fun searchUserError() {
        val userId = factory.getNumberBetween(1, 50).toString()
        val expectedGreeting = "No user detected"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromApi(userId))
            .thenReturn(Completable.complete().delay(loadingDelay, MILLISECONDS).andThen(Single.error(Exception())))

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_user_id).perform(typeText(userId))
        viewWithId(R.id.btn_search).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertGreetingIsShown(expectedGreeting)

        // Verify behaviour
        verify(tempGreeter).greeting()
        verifyNoMoreInteractions(tempGreeter)

        scenario.close()
    }

    private fun launchActivity() {
        val expectedGreeting = "Hello World!"

        // Mock behaviour
        `when`(tempGreeter.greeting()).thenReturn(expectedGreeting)

        scenario = launchActivity<MainActivity>()

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(1500)

        assertGreetingIsShown(expectedGreeting)
    }

    private fun assertLoading() {
        viewWithId(R.id.text_greeting).check(assertIsGone())
        viewWithId(R.id.progress_loading).check(assertIsVisible())
    }

    private fun assertGreetingIsShown(expectedGreeting: String) {
        viewWithId(R.id.text_greeting).check(assertIsVisible())
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))
        viewWithId(R.id.progress_loading).check(assertIsGone())
    }
}
