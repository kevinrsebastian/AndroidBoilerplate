package com.fivenapp.holo.view.loan.schedule

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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class LoanScheduleListActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: LoanScheduleListActivityVm
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

        classUnderTest = Mockito.spy(LoanScheduleListActivityVm(rxSchedulerUtils))
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        mockitoClosable.close()
    }

    /** Method: [LoanScheduleListActivityVm.loadLoanSchedules] */
    @Test
    fun loadLoanSchedules() {
        // Assert initial state
        assertThat(classUnderTest.isLoading.get()).isFalse

        // Execute the function being tested
        val loadingDelay = 1000L
        classUnderTest.loadLoanSchedules(loadingDelay)

        // Advance the time by a slight delay to assert that loading started
        testScheduler.advanceTimeBy(100L, TimeUnit.MILLISECONDS)
        assertThat(classUnderTest.isLoading.get()).isTrue

        // Advance time by the delay to assert that loading finished
        testScheduler.advanceTimeBy(loadingDelay, TimeUnit.MILLISECONDS)
        assertThat(classUnderTest.isLoading.get()).isFalse

        // Verify behaviour
        verify(classUnderTest, Mockito.times(3)).isLoading // Called by this test
        verify(classUnderTest).loadLoanSchedules(loadingDelay)
        verify(rxSchedulerUtils).completableAsyncSchedulerTransformer()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions()
    }

    private fun verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(classUnderTest, rxSchedulerUtils)
    }
}
