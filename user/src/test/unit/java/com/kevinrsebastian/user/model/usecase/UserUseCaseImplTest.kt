package com.kevinrsebastian.user.model.usecase

import com.google.gson.Gson
import com.kevinrsebastian.user.model.api.MockUserApi
import com.kevinrsebastian.user.model.data.User
import com.kevinrsebastian.user.model.data.UserEntity
import com.kevinrsebastian.user.model.db.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
internal class UserUseCaseImplTest {

    private val factory = DataFactory()

    private lateinit var classUnderTest: UserUseCaseImpl

    private lateinit var server: MockWebServer

    // Dependencies
    private lateinit var mockUserApi: MockUserApi
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        // Set up a mock server
        server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        mockUserApi = spy(retrofit.create(MockUserApi::class.java))
        userDao = mock(UserDao::class.java, Mockito.CALLS_REAL_METHODS)
        classUnderTest = spy(UserUseCaseImpl(mockUserApi, userDao))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    // =================================================================================================================
    // API functions
    // =================================================================================================================

    /**
     * Method: [UserUseCaseImpl.getUsersFromApi]
     * Scenario: Success
     */
    @Test
    fun getUsersFromApi() {
        val user1 = User("1", factory.firstName, factory.lastName)
        val user2 = User("2", factory.firstName, factory.lastName)
        val user3 = User("3", factory.firstName, factory.lastName)
        val expectedUsers = arrayListOf(user1, user2, user3)

        // Load mock response
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(expectedUsers))
        )

        // Execute function to be tested
        val actualUsers = classUnderTest.getUsersFromApi().blockingGet()

        // Assertions
        assertEqual(actualUsers, expectedUsers)

        // Verify Behaviour
        verify(classUnderTest).getUsersFromApi() // Called by this test
        verify(mockUserApi).getAllUsers()
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.getUserFromApi]
     * Scenario: Success
     */
    @Test
    fun getUserFromApiSuccess() {
        val expectedUser = User("1", factory.firstName, factory.lastName)

        // Load mock response
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(expectedUser))
        )

        // Execute function to be tested
        val actualUser = classUnderTest.getUserFromApi(expectedUser.id).blockingGet()

        // Assertions
        assertEqual(actualUser, expectedUser)

        // Verify Behaviour
        verify(classUnderTest).getUserFromApi(expectedUser.id) // Called by this test
        verify(mockUserApi).getUser(expectedUser.id)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.getUserFromApi]
     * Scenario: An error will be returned because there is no user with matching ID.
     */
    @Test
    fun getUserFromApiError() {
        val userId = "1"
        val expectedErrorMessage = "User not found"

        // Load mock response
        server.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody(expectedErrorMessage)
        )

        // Execute function to be tested
        classUnderTest.getUserFromApi(userId)
            .test()
            .await()
            .assertNotComplete()
            .assertError(HttpException::class.java)

        // Verify Behaviour
        verify(classUnderTest).getUserFromApi(userId) // Called by this test
        verify(mockUserApi).getUser(userId)
        verifyNoMoreInteractions()
    }

    // =================================================================================================================
    // DB functions
    // =================================================================================================================

    /**
     * Method: [UserUseCaseImpl.addUserToDb]
     * Scenario: Success
     */
    @Test
    fun addUserToDb() {
        val user = User("1", factory.firstName, factory.lastName)
        val userEntity = UserEntity(user.id.toInt(), user.firstName, user.lastName)

        // Mock behaviour
        `when`(userDao.insertUser(userEntity)).thenReturn(Completable.complete())

        // Execute function to be tested
        classUnderTest.addUserToDb(user).test().await().assertComplete()

        // Verify Behaviour
        verify(classUnderTest).addUserToDb(user) // Called by this test
        verify(userDao).insertUser(userEntity)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.getUserFromDb]
     * Scenario: Success
     */
    @Test
    fun getUserFromDbSuccess() {
        val expectedUser = User("1", factory.firstName, factory.lastName)
        val userEntity = UserEntity(expectedUser.id.toInt(), expectedUser.firstName, expectedUser.lastName)

        // Mock behaviour
        `when`(userDao.getUser(expectedUser.id.toInt())).thenReturn(Single.just(userEntity))

        // Execute function to be tested
        val actualUser = classUnderTest.getUserFromDb(expectedUser.id).blockingGet()

        // Assertions
        assertEqual(actualUser, expectedUser)

        // Verify Behaviour
        verify(classUnderTest).getUserFromDb(expectedUser.id) // Called by this test
        verify(userDao).getUser(expectedUser.id.toInt())
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.getUserFromDb]
     * Scenario: Error will occur because passed userId is a non-numeric string.
     */
    @Test
    fun getUserFromDbError() {
        val userId = "a"

        // Execute function to be tested
        classUnderTest.getUserFromDb(userId)
            .test()
            .assertNotComplete()
            .await()
            .assertError(NumberFormatException::class.java)

        // Verify Behaviour
        verify(classUnderTest).getUserFromDb(userId) // Called by this test
        verifyNoMoreInteractions()
    }

    private fun assertEqual(users1: List<User>, users2: List<User>) {
        assertThat(users1).hasSameSizeAs(users2)
        for (i in users1.indices) {
            assertEqual(users1[i], users2[i])
        }
    }

    private fun assertEqual(user1: User, user2: User) {
        assertThat(user1.id).isEqualTo(user2.id)
        assertThat(user1.firstName).isEqualTo(user2.firstName)
        assertThat(user1.lastName).isEqualTo(user2.lastName)
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(mockUserApi, userDao)
    }
}
