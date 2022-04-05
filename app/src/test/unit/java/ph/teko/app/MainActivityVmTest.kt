package ph.teko.app

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import ph.teko.app.extension.getOrAwaitValue
import ph.teko.app.extension.hasNotBeenSet
import ph.teko.app.temp.TempService
import ph.teko.app.test.base.BaseUnitTest
import ph.teko.app.test.factory.UserFactory
import ph.teko.app.user_model.data.User
import ph.teko.app.user_model.usecase.UserUseCase
import ph.teko.app.util.rx.SyncRxSchedulerUtils
import java.util.concurrent.TimeUnit.MILLISECONDS

@RunWith(JUnit4::class)
internal class MainActivityVmTest : BaseUnitTest() {

    private lateinit var classUnderTest: MainActivityVm
    private var rxSchedulerUtils: SyncRxSchedulerUtils = SyncRxSchedulerUtils()
    private lateinit var tempService: TempService
    private lateinit var userUseCase: UserUseCase

    @Before
    override fun setUp() {
        super.setUp()
        rxSchedulerUtils = spy(SyncRxSchedulerUtils())
        tempService = Mockito.mock(TempService::class.java)
        userUseCase = Mockito.mock(UserUseCase::class.java)
        classUnderTest = spy(MainActivityVm(rxSchedulerUtils, tempService, userUseCase))
    }

    /** Method: [MainActivityVm.loadGreeting] */
    @Test
    fun loadGreeting() {
        val expectedGreeting = "Hello Jose!"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(tempService.getGreeting()).thenReturn(Single.just(expectedGreeting))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.loadGreeting(loadingDelay)

        // Load a slight delay to assert the loading state
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by by the delay
        testScheduler.advanceTimeBy(loadingDelay + 100L, MILLISECONDS)

        // Assert the states
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.getOrAwaitValue()).isEqualTo(expectedGreeting)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading // Called by this test
        verify(classUnderTest, times(2)).greeting // Called by this test
        verify(classUnderTest).loadGreeting(loadingDelay) // Called by this test
        verify(classUnderTest).setGreeting(expectedGreeting)
        verify(tempService).getGreeting()
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /** Method: [MainActivityVm.setGreetingSubject] */
    @Test
    fun setGreetingSubject() {
        val newSubject = DataFactory().name

        // Mock behaviour
        `when`(tempService.setGreetingSubject(newSubject)).thenReturn(Completable.complete())

        assertInitialState()

        // Execute the function being tested
        classUnderTest.setGreetingSubject(newSubject)

        // Verify behaviour
        verify(classUnderTest).isLoading // Called by this test
        verify(classUnderTest).greeting // Called by this test
        verify(classUnderTest).setGreetingSubject(newSubject) // Called by this test
        verify(tempService).setGreetingSubject(newSubject)
        verify(rxSchedulerUtils).completableAsyncSchedulerTransformer()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [MainActivityVm.greetApiUserWithId]
     * Scenario: Success
     */
    @Test
    fun greetApiUserWithIdSuccess() {
        val expectedUser = UserFactory.user()
        val expectedGreeting = "Hello ${expectedUser.firstName} ${expectedUser.lastName}!"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromApi(expectedUser.id))
            .thenReturn(Single.just(expectedUser).delay(loadingDelay, MILLISECONDS))
        `when`(userUseCase.addUserToDb(expectedUser))
            .thenReturn(Completable.complete())

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetApiUserWithId(expectedUser.id)

        // Load a slight delay to assert the loading state
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by by the delay
        testScheduler.advanceTimeBy(loadingDelay, MILLISECONDS)

        // Assert the states
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.getOrAwaitValue()).isEqualTo(expectedGreeting)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading // Called by this test
        verify(classUnderTest, times(2)).greeting // Called by this test
        verify(classUnderTest).greetApiUserWithId(expectedUser.id) // Called by this test
        verify(classUnderTest).setGreeting(expectedGreeting)
        verify(userUseCase).getUserFromApi(expectedUser.id)
        verify(userUseCase).addUserToDb(expectedUser)
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [MainActivityVm.greetApiUserWithId]
     * Scenario: [UserUseCase.getUserFromApi] will return an error.
     */
    @Test
    fun greetApiUserWithIdError() {
        val expectedErrorMessage = "No user detected"
        val userId = UserFactory.user().id
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromApi(userId))
            .thenReturn(Single.error<User>(Exception("Error message")).delay(loadingDelay, MILLISECONDS))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetApiUserWithId(userId)

        // Load a slight delay to assert the loading state
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by by the delay
        testScheduler.advanceTimeBy(loadingDelay, MILLISECONDS)

        // Assert the states
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.getOrAwaitValue()).isEqualTo(expectedErrorMessage)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading // Called by this test
        verify(classUnderTest, times(2)).greeting // Called by this test
        verify(classUnderTest).greetApiUserWithId(userId) // Called by this test
        verify(classUnderTest).setGreeting(expectedErrorMessage)
        verify(userUseCase).getUserFromApi(userId)
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [MainActivityVm.greetDbUserWithId]
     * Scenario: Success
     */
    @Test
    fun greetDbUserWithIdSuccess() {
        val expectedUser = UserFactory.user()
        val expectedGreeting = "Hello ${expectedUser.firstName} ${expectedUser.lastName}!"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromDb(expectedUser.id))
            .thenReturn(Single.just(expectedUser).delay(loadingDelay, MILLISECONDS))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetDbUserWithId(expectedUser.id)

        // Load a slight delay to assert the loading state
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by by the delay
        testScheduler.advanceTimeBy(loadingDelay, MILLISECONDS)

        // Assert the states
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.getOrAwaitValue()).isEqualTo(expectedGreeting)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading // Called by this test
        verify(classUnderTest, times(2)).greeting // Called by this test
        verify(classUnderTest).greetDbUserWithId(expectedUser.id) // Called by this test
        verify(classUnderTest).setGreeting(expectedGreeting)
        verify(userUseCase).getUserFromDb(expectedUser.id)
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [MainActivityVm.greetDbUserWithId]
     * Scenario: [UserUseCase.getUserFromDb] will return an error.
     */
    @Test
    fun greetDbUserWithIdError() {
        val expectedErrorMessage = "No user detected"
        val userId = UserFactory.user().id
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromDb(userId))
            .thenReturn(Single.error<User>(Exception("Error message")).delay(loadingDelay, MILLISECONDS))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetDbUserWithId(userId)

        // Load a slight delay to assert the loading state
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by by the delay
        testScheduler.advanceTimeBy(loadingDelay, MILLISECONDS)

        // Assert the states
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.getOrAwaitValue()).isEqualTo(expectedErrorMessage)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading // Called by this test
        verify(classUnderTest, times(2)).greeting // Called by this test
        verify(classUnderTest).greetDbUserWithId(userId) // Called by this test
        verify(classUnderTest).setGreeting(expectedErrorMessage)
        verify(userUseCase).getUserFromDb(userId)
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    private fun assertInitialState() {
        assertThat(classUnderTest.isLoading.get()).isFalse
        assertThat(classUnderTest.greeting.hasNotBeenSet()).isTrue
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(classUnderTest, rxSchedulerUtils, tempService)
    }
}
