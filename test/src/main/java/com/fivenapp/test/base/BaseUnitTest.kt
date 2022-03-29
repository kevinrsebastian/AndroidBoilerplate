package com.fivenapp.test.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations

/**
 * Base class for unit tests. Handles the following:
 * - InstantTaskExecutorRule
 * - Mockito Annotation mock creation and closing
 * - TestScheduler for advancing Rxjava threads (https://medium.com/mindorks/small-things-when-unit-testing-rxjava-in-android-7f7c336ccabd)
 */
@RunWith(JUnit4::class)
open class BaseUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var mockitoClosable: AutoCloseable
    lateinit var testScheduler: TestScheduler

    @Before
    open fun setUp() {
        mockitoClosable = MockitoAnnotations.openMocks(this)

        // Set up TestScheduler for advancing the time
        RxJavaPlugins.reset()
        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @After
    open fun tearDown() {
        RxJavaPlugins.reset()
        mockitoClosable.close()
    }
}
