package com.fivenapp.holo.view.loan.schedule

import com.fivenapp.holo.util.rx.SyncRxSchedulerUtils
import com.fivenapp.test.base.BaseUnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
internal class LoanScheduleListActivityVmTest : BaseUnitTest() {

    private lateinit var classUnderTest: LoanScheduleListActivityVm
    private lateinit var rxSchedulerUtils: SyncRxSchedulerUtils

    @Before
    override fun setUp() {
        super.setUp()
        rxSchedulerUtils = spy(SyncRxSchedulerUtils())
        classUnderTest = spy(LoanScheduleListActivityVm(rxSchedulerUtils))
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
