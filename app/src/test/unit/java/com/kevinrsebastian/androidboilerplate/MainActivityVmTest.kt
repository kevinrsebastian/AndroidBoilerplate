package com.kevinrsebastian.androidboilerplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevinrsebastian.androidboilerplate.extension.getOrAwaitValue
import com.kevinrsebastian.androidboilerplate.extension.hasNotBeenSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class MainActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: MainActivityVm

    @Test
    fun loadGreeting() {
        val expectedGreeting = "Hello World!"
        val loadingDelay = 1000L
        classUnderTest = MainActivityVm()

        // Assert initial states
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().hasNotBeenSet()).isTrue

        // Execute the function being tested
        classUnderTest.loadGreeting(expectedGreeting, loadingDelay)

        // Load a slight delay to assert the loading state
        Thread.sleep(100L)
        assertThat(classUnderTest.isLoading().get()).isTrue

        // Load the delay and assert the states
        Thread.sleep(loadingDelay)
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().getOrAwaitValue()).isEqualTo(expectedGreeting)
    }
}
