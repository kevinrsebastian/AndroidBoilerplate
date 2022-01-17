package com.kevinrsebastian.androidboilerplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevinrsebastian.androidboilerplate.extension.getOrAwaitValue
import com.kevinrsebastian.androidboilerplate.extension.hasNotBeenSet
import com.kevinrsebastian.androidboilerplate.temp.TempGreeter
import com.kevinrsebastian.androidboilerplate.temp.TempGreeterImpl
import com.kevinrsebastian.androidboilerplate.temp.TempService
import com.kevinrsebastian.androidboilerplate.temp.TempServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class MainActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: MainActivityVm

    // Dependencies
    // TODO: Replace these with mocks later on
    private lateinit var tempGreeter: TempGreeter
    private lateinit var tempService: TempService

    @Before
    fun setUp() {
        // Initialize classUnderTest and dependencies
        tempGreeter = TempGreeterImpl("Jose")
        tempService = TempServiceImpl(tempGreeter)
        classUnderTest = MainActivityVm(tempService)
    }

    @Test
    fun loadGreeting() {
        val expectedGreeting = "Hello Jose!"
        val loadingDelay = 1000L

        // Assert initial states
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().hasNotBeenSet()).isTrue

        // Execute the function being tested
        classUnderTest.loadGreeting(loadingDelay)

        // Load a slight delay to assert the loading state
        Thread.sleep(200L)
        assertThat(classUnderTest.isLoading().get()).isTrue

        // Load the delay and assert the states
        Thread.sleep(loadingDelay)
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().getOrAwaitValue()).isEqualTo(expectedGreeting)
    }

    @Test
    fun setGreetingSubject() {
        val newSubject = DataFactory().name
        val expectedGreeting = "Hello $newSubject!"
        val loadingDelay = 1000L

        // Assert initial states
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().hasNotBeenSet()).isTrue

        // Execute the function being tested
        classUnderTest.setGreetingSubject(newSubject)
        classUnderTest.loadGreeting(loadingDelay)

        // Load a slight delay to assert the loading state
        Thread.sleep(200L)
        assertThat(classUnderTest.isLoading().get()).isTrue

        // Load the delay and assert the states
        Thread.sleep(loadingDelay)
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().getOrAwaitValue()).isEqualTo(expectedGreeting)
    }
}
