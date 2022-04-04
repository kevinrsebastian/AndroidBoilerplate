package com.kevinrsebastian.androidboilerplate

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertHasText
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsDisabled
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsEnabled
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsGone
import com.kevinrsebastian.androidboilerplate.testutil.AssertionTestUtils.assertIsVisible
import com.kevinrsebastian.androidboilerplate.testutil.ViewTestUtils.viewWithId
import com.kevinrsebastian.test.base.BaseInstrumentationTest
import com.kevinrsebastian.test.factory.UserFactory
import com.kevinrsebastian.user.model.usecase.UserUseCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class MainActivityTest : BaseInstrumentationTest() {

    @Inject
    lateinit var tempGreeter: TempGreeter

    @Inject
    lateinit var userUseCase: UserUseCase

    private lateinit var scenario: ActivityScenario<MainActivity>

    @After
    override fun tearDown() {
        super.tearDown()
        scenario.close()
    }

    @Test
    fun launchDefault() {
        launchActivity()

        // Verify behaviour
        verify(tempGreeter).greeting()
        verifyNoMoreInteractions(tempGreeter)
    }

    @Test
    fun searchExistingUserInApi() {
        val user = UserFactory.user()
        val expectedGreeting = "Hello ${user.firstName} ${user.lastName}!"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromApi(user.id))
            .thenReturn(Single.just(user).delay(loadingDelay, MILLISECONDS))
        `when`(userUseCase.addUserToDb(user))
            .thenReturn(Completable.complete())

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_api_user_id).perform(typeText(user.id))
        viewWithId(R.id.btn_search_api).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertNotLoading()
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))

        // Verify behaviour
        verify(tempGreeter).greeting()
        verify(userUseCase).getUserFromApi(user.id)
        verify(userUseCase).addUserToDb(user)
        verifyNoMoreInteractions()
    }

    @Test
    fun searchUserInApiError() {
        val userId = UserFactory.user().id
        val expectedGreeting = "No user detected"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromApi(userId))
            .thenReturn(Completable.complete().delay(loadingDelay, MILLISECONDS).andThen(Single.error(Exception())))

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_api_user_id).perform(typeText(userId))
        viewWithId(R.id.btn_search_api).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertNotLoading()
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))

        // Verify behaviour
        verify(tempGreeter).greeting()
        verify(userUseCase).getUserFromApi(userId)
        verifyNoMoreInteractions()
    }

    @Test
    fun searchExistingUserInDb() {
        val user = UserFactory.user()
        val expectedGreeting = "Hello ${user.firstName} ${user.lastName}!"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromDb(user.id))
            .thenReturn(Single.just(user).delay(loadingDelay, MILLISECONDS))

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_db_user_id).perform(typeText(user.id))
        viewWithId(R.id.btn_search_db).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertNotLoading()
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))

        // Verify behaviour
        verify(tempGreeter).greeting()
        verify(userUseCase).getUserFromDb(user.id)
        verifyNoMoreInteractions()
    }

    @Test
    fun searchUserInDbError() {
        val userId = UserFactory.user().id
        val expectedGreeting = "No user detected"

        // Mock behaviour
        val loadingDelay = 1000L
        `when`(userUseCase.getUserFromDb(userId))
            .thenReturn(Completable.complete().delay(loadingDelay, MILLISECONDS).andThen(Single.error(Exception())))

        launchActivity()

        // Enter user ID and click submit
        viewWithId(R.id.edit_db_user_id).perform(typeText(userId))
        viewWithId(R.id.btn_search_db).perform(ViewActions.click())

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(loadingDelay)

        assertNotLoading()
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))

        // Verify behaviour
        verify(tempGreeter).greeting()
        verify(userUseCase).getUserFromDb(userId)
        verifyNoMoreInteractions()
    }

    private fun launchActivity() {
        val expectedGreeting = "Hello World!"

        // Mock behaviour
        `when`(tempGreeter.greeting()).thenReturn(expectedGreeting)

        scenario = launchActivity<MainActivity>()

        // Assert loading and wait for delay
        assertLoading()
        Thread.sleep(1500)

        assertNotLoading()
        viewWithId(R.id.text_greeting).check(assertHasText(expectedGreeting))
    }

    private fun assertLoading() {
        viewWithId(R.id.text_greeting).check(assertIsGone())
        viewWithId(R.id.progress_loading).check(assertIsVisible())
        viewWithId(R.id.btn_search_api).check(assertIsDisabled())
        viewWithId(R.id.btn_search_db).check(assertIsDisabled())
    }

    private fun assertNotLoading() {
        viewWithId(R.id.text_greeting).check(assertIsVisible())
        viewWithId(R.id.progress_loading).check(assertIsGone())
        viewWithId(R.id.btn_search_api).check(assertIsEnabled())
        viewWithId(R.id.btn_search_db).check(assertIsEnabled())
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(tempGreeter, userUseCase)
    }
}
