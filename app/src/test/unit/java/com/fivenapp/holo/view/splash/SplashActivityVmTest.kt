package com.fivenapp.holo.view.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fivenapp.holo.util.rx.SyncRxSchedulerUtils
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class SplashActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: SplashActivityVm
    private lateinit var mockitoClosable: AutoCloseable
    private lateinit var testScheduler: TestScheduler

    // Dependencies
    @Spy
    private var rxSchedulerUtils: SyncRxSchedulerUtils = SyncRxSchedulerUtils()

    @Before
    fun setUp() {
        mockitoClosable = MockitoAnnotations.openMocks(this)

        // Set up TestScheduler for advancing the time
        RxJavaPlugins.reset()
        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        classUnderTest = Mockito.spy(SplashActivityVm(rxSchedulerUtils))
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        mockitoClosable.close()
    }

    /** Method: [SplashActivityVm.loadDelay] */
    @Test
    fun loadDelay() {
        // Mock onSuccess callback
        val function: () -> Unit = {}
        val onSuccess = mock(function::class.java)

        // Assert initial state
        assertThat(classUnderTest.isLoading.get()).isFalse

        // Execute the function being tested
        val loadingDelay = 1000L
        classUnderTest.loadDelay(loadingDelay, onSuccess)

        // Advance the time by a slight delay to assert that loading started
        testScheduler.advanceTimeBy(100L, TimeUnit.MILLISECONDS)
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by the delay to assert that loading finished
        testScheduler.advanceTimeBy(loadingDelay, TimeUnit.MILLISECONDS)
        assertThat(classUnderTest.isLoading.get()).isFalse

        // Verify behaviour
        verify(classUnderTest, Mockito.times(3)).isLoading // Called by this test
        verify(classUnderTest).loadDelay(loadingDelay, onSuccess)
        verify(rxSchedulerUtils).completableAsyncSchedulerTransformer()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verify(onSuccess).invoke()
        verifyNoMoreInteractions(onSuccess)
        verifyNoMoreInteractions()
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(classUnderTest, rxSchedulerUtils)
    }
}