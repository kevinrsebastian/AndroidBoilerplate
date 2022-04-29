package ph.teko.app.vm

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import ph.teko.app.test.base.BaseUnitTest
import ph.teko.app.test.factory.UserFactory
import ph.teko.app.user_model.data.User
import ph.teko.app.user_model.usecase.UserUseCase

@RunWith(JUnit4::class)
internal class SplashActivityVmTest : BaseUnitTest() {

    private lateinit var classUnderTest: SplashActivityVm
    private lateinit var userUseCase: UserUseCase

    @Before
    override fun setUp() {
        super.setUp()
        userUseCase = Mockito.mock(UserUseCase::class.java)
        classUnderTest = spy(SplashActivityVm(userUseCase))
    }

    /**
     * Method: [SplashActivityVm.checkForLoggedInUser]
     * Scenario: A logged-in user is found. Should navigate to the home screen.
     */
    @Test
    fun checkForLoggedInUserSuccess() {
        val function: () -> Unit = { }
        val navigateToHomeScreen = Mockito.mock(function::class.java)

        // Mock return a user
        val user = UserFactory.user()
        executeCheckForLoggedInUser(user, navigateToHomeScreen)

        // Verify behaviour
        verifyCheckForLoggedInUserCommonBehaviour(navigateToHomeScreen)
        verify(navigateToHomeScreen).invoke()
        verifyNoMoreInteractions(classUnderTest, navigateToHomeScreen)
    }

    /**
     * Method: [SplashActivityVm.checkForLoggedInUser]
     * Scenario: A logged-in user is found. Should stay ont he current screen
     */
    @Test
    fun checkForLoggedInUserFailure() {
        val function: () -> Unit = { }
        val navigateToHomeScreen = Mockito.mock(function::class.java)

        // Mock return a null user
        val user = null
        executeCheckForLoggedInUser(user, navigateToHomeScreen)

        // Verify behaviour
        verifyCheckForLoggedInUserCommonBehaviour(navigateToHomeScreen)
        verifyNoInteractions(navigateToHomeScreen)
        verifyNoMoreInteractions(classUnderTest)
    }

    private fun executeCheckForLoggedInUser(user: User?, navigateToHomeScreen: () -> Unit) {
        // Mock behavior
        `when`(userUseCase.getUserFromCache()).thenReturn(user)

        // Assert initial state
        assertThat(classUnderTest.isLoading.get()).isFalse

        // Execute the function being tested
        classUnderTest.checkForLoggedInUser(navigateToHomeScreen)

        // Assert final state
        assertThat(classUnderTest.isLoading.get()).isFalse
    }

    private fun verifyCheckForLoggedInUserCommonBehaviour(navigateToHomeScreen: () -> Unit) {
        verify(classUnderTest).checkForLoggedInUser(navigateToHomeScreen) // Called by this test
        verify(classUnderTest).setLoading(true)
        verify(userUseCase).getUserFromCache()
        verify(classUnderTest).setLoading(false)
        verify(classUnderTest, times(2)).isLoading // Called by this test
    }
}
