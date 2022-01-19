package com.kevinrsebastian.androidboilerplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevinrsebastian.androidboilerplate.extension.getOrAwaitValue
import com.kevinrsebastian.androidboilerplate.extension.hasNotBeenSet
import com.kevinrsebastian.androidboilerplate.temp.TempService
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

@RunWith(JUnit4::class)
class MainActivityVmTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: MainActivityVm

    private lateinit var mockitoClosable: AutoCloseable

    @Mock
    private lateinit var tempService: TempService

    @Before
    fun setUp() {
        mockitoClosable = MockitoAnnotations.openMocks(this)
        classUnderTest = spy(MainActivityVm(tempService))
    }

    @After
    fun tearDown() {
        mockitoClosable.close()
    }

    @Test
    fun loadGreeting() {
        val expectedGreeting = "Hello Jose!"
        val loadingDelay = 1000L

        // Mock behaviour
        `when`(tempService.getGreeting()).thenReturn(expectedGreeting)

        assertInitialState()

        // Execute the function being tested
        classUnderTest.loadGreeting(loadingDelay)

        // Load a slight delay to assert the loading state
        Thread.sleep(200L)
        assertThat(classUnderTest.isLoading().get()).isTrue

        // Load the delay and assert the states
        Thread.sleep(loadingDelay)
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().getOrAwaitValue()).isEqualTo(expectedGreeting)

        // Verify behaviour
        verify(classUnderTest, times(3)).isLoading() // Called by this test
        verify(classUnderTest, times(2)).getGreeting() // Called by this test
        verify(classUnderTest).loadGreeting(loadingDelay) // Called by this test
        verify(classUnderTest).setGreeting(expectedGreeting)
        verify(tempService).getGreeting()
        verify(classUnderTest).setLoading(true)
        verify(classUnderTest).setLoading(false)
        verifyNoMoreInteractions(classUnderTest, tempService)
    }

    @Test
    fun setGreetingSubject() {
        val newSubject = DataFactory().name

        assertInitialState()

        // Execute the function being tested
        classUnderTest.setGreetingSubject(newSubject)

        // Verify behaviour
        verify(classUnderTest).isLoading() // Called by this test
        verify(classUnderTest).getGreeting() // Called by this test
        verify(classUnderTest).setGreetingSubject(newSubject) // Called by this test
        verify(tempService).setGreetingSubject(newSubject)
        verifyNoMoreInteractions(classUnderTest, tempService)
    }

    private fun assertInitialState() {
        assertThat(classUnderTest.isLoading().get()).isFalse
        assertThat(classUnderTest.getGreeting().hasNotBeenSet()).isTrue
    }
}
