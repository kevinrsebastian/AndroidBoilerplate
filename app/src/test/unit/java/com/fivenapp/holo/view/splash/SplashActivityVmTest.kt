package com.fivenapp.holo.view.splash

import com.fivenapp.holo.util.rx.SyncRxSchedulerUtils
import com.fivenapp.test.base.BaseUnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class SplashActivityVmTest : BaseUnitTest() {

    private lateinit var classUnderTest: SplashActivityVm
    private lateinit var rxSchedulerUtils: SyncRxSchedulerUtils

    @Before
    override fun setUp() {
        super.setUp()
        rxSchedulerUtils = Mockito.spy(SyncRxSchedulerUtils())
        classUnderTest = Mockito.spy(SplashActivityVm(rxSchedulerUtils))
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
