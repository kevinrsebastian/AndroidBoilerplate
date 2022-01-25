package com.kevinrsebastian.androidboilerplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevinrsebastian.androidboilerplate.extension.getOrAwaitValue
import com.kevinrsebastian.androidboilerplate.extension.hasNotBeenSet
import com.kevinrsebastian.androidboilerplate.model.data.User
import com.kevinrsebastian.androidboilerplate.model.usecase.UserUseCase
import com.kevinrsebastian.androidboilerplate.temp.TempService
import com.kevinrsebastian.androidboilerplate.util.rx.SyncRxSchedulerUtils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import java.util.concurrent.TimeUnit.MILLISECONDS

@RunWith(JUnit4::class)
class MainActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: MainActivityVm

    private val factory = DataFactory()

    private lateinit var mockitoClosable: AutoCloseable

    private lateinit var testScheduler: TestScheduler

    // Dependencies
    @Spy
    private var rxSchedulerUtils: SyncRxSchedulerUtils = SyncRxSchedulerUtils()
    @Mock
    private lateinit var tempService: TempService
    @Mock
    private lateinit var userUseCase: UserUseCase

    @Before
    fun setUp() {
        mockitoClosable = MockitoAnnotations.openMocks(this)

        // Set up TestScheduler for advancing the time
        RxJavaPlugins.reset()
        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        classUnderTest = spy(MainActivityVm(rxSchedulerUtils, tempService, userUseCase))
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        mockitoClosable.close()
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
     * Method: [MainActivityVm.greetUserWithId]
     * Scenario: Success
     */
    @Test
    fun greetUserWithIdSuccess() {
        val expectedUser = User("1", factory.firstName, factory.lastName)
        val expectedGreeting = "Hello ${expectedUser.firstName} ${expectedUser.lastName}!"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromApi(expectedUser.id))
            .thenReturn(Single.just(expectedUser).delay(loadingDelay, MILLISECONDS))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetUserWithId(expectedUser.id)

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
        verify(classUnderTest).greetUserWithId(expectedUser.id) // Called by this test
        verify(classUnderTest).setGreeting(expectedGreeting)
        verify(userUseCase).getUserFromApi(expectedUser.id)
        verify(rxSchedulerUtils).singleAsyncSchedulerTransformer<String>()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [MainActivityVm.greetUserWithId]
     * Scenario: [UserUseCase.getUserFromApi] will return an error.
     */
    @Test
    fun greetUserWithIdError() {
        val expectedErrorMessage = "No user detected"
        val userId = "1"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(userUseCase.getUserFromApi(userId))
            .thenReturn(Single.error<User>(Exception("Error message")).delay(loadingDelay, MILLISECONDS))

        assertInitialState()

        // Execute the function being tested
        classUnderTest.greetUserWithId(userId)

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
        verify(classUnderTest).greetUserWithId(userId) // Called by this test
        verify(classUnderTest).setGreeting(expectedErrorMessage)
        verify(userUseCase).getUserFromApi(userId)
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
