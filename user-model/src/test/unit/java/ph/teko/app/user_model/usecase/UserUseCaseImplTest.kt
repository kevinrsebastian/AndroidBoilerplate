package ph.teko.app.user_model.usecase

import com.google.gson.Gson
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
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import ph.teko.app.core.preferences.SharedPrefUtils
import ph.teko.app.test.assertion.UserAssertion.assertEqual
import ph.teko.app.test.base.BaseUnitTest
import ph.teko.app.test.factory.UserFactory
import ph.teko.app.user_model.api.MockUserApi
import ph.teko.app.user_model.api.UserApi
import ph.teko.app.user_model.api.UserResponse
import ph.teko.app.user_model.constants.SHARED_PREF_USER_KEY
import ph.teko.app.user_model.db.UserDao
import ph.teko.app.user_model.db.UserEntity
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
internal class UserUseCaseImplTest : BaseUnitTest() {

    private lateinit var classUnderTest: UserUseCaseImpl
    private lateinit var mockUserApi: MockUserApi
    private lateinit var server: MockWebServer
    private lateinit var sharedPrefUtils: SharedPrefUtils
    private lateinit var userApi: UserApi
    private lateinit var userDao: UserDao
    private val dataFactory = DataFactory()
    private val gson = Gson()

    @Before
    override fun setUp() {
        super.setUp()

        // Set up a mock server
        server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        mockUserApi = spy(retrofit.create(MockUserApi::class.java))
        sharedPrefUtils = mock(SharedPrefUtils::class.java)
        userApi = spy(retrofit.create(UserApi::class.java))
        userDao = mock(UserDao::class.java, Mockito.CALLS_REAL_METHODS)
        classUnderTest = spy(UserUseCaseImpl(mockUserApi, sharedPrefUtils, userApi, userDao))
    }

    @After
    override fun tearDown() {
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
        val user1 = UserFactory.user("1")
        val user2 = UserFactory.user("2")
        val user3 = UserFactory.user("3")
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
        val expectedUser = UserFactory.user()

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
        val userId = UserFactory.user().id
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

    /**
     * Method: [UserUseCaseImpl.login]
     * Scenario: Login API is a success
     */
    @Test
    fun loginSuccess() {
        val expectedUser = UserFactory.user()
        val userResponse = UserResponse.fromUser(expectedUser)
        val loginEmail = dataFactory.emailAddress
        val loginPassword = dataFactory.getRandomChars(10)

        // Load mock response
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(userResponse))
        )

        // Execute function to be tested
        val actualUser = classUnderTest.login(loginEmail, loginPassword).blockingGet()

        // Assertions
        assertEqual(expectedUser, actualUser)

        // Verify behaviour
        verify(classUnderTest).login(loginEmail, loginPassword) // Called by this test
        verify(userApi).login(loginEmail, loginPassword, true)
        verify(sharedPrefUtils).setString(SHARED_PREF_USER_KEY, Gson().toJson(expectedUser))
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.login]
     * Scenario: Login API fails
     */
    @Test
    fun loginFailure() {
        val loginEmail = dataFactory.emailAddress
        val loginPassword = dataFactory.getRandomChars(10)

        // Load mock response
        server.enqueue(MockResponse().setResponseCode(404))

        // Execute function to be tested
        classUnderTest.login(loginEmail, loginPassword)
            .test()
            .await()
            .assertNotComplete()
            .assertError(Throwable::class.java)

        // Verify behaviour
        verify(classUnderTest).login(loginEmail, loginPassword) // Called by this test
        verify(userApi).login(loginEmail, loginPassword, true)
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.checkLogin]
     * Scenario: CheckLogin API is a success
     */
    @Test
    fun checkLoginSuccess() {
        val expectedUser = UserFactory.user()
        val userResponse = UserResponse.fromUser(expectedUser)

        // Load mock response
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Gson().toJson(userResponse))
        )

        // Execute function to be tested
        val actualUser = classUnderTest.checkLogin().blockingGet()

        // Assertions
        assertEqual(expectedUser, actualUser)

        // Verify behaviour
        verify(classUnderTest).checkLogin() // Called by this test
        verify(userApi).checkLogin()
        verifyNoMoreInteractions()
    }

    /**
     * Method: [UserUseCaseImpl.checkLogin]
     * Scenario: CheckLogin API fails
     */
    @Test
    fun checkLoginFailure() {
        val expectedUser = UserFactory.user()

        // Load mock response
        server.enqueue(MockResponse().setResponseCode(404))

        // Execute function to be tested
        classUnderTest.checkLogin()
            .test()
            .await()
            .assertNotComplete()
            .assertError(Throwable::class.java)

        // Verify behaviour
        verify(classUnderTest).checkLogin() // Called by this test
        verify(userApi).checkLogin()
        verifyNoMoreInteractions()
    }

    // =================================================================================================================
    // Cache functions
    // =================================================================================================================

    /** Method: [UserUseCaseImpl.clearUserFromCache] */
    @Test
    fun clearUserFromCache() {
        // Mock behaviour
        doNothing().`when`(sharedPrefUtils).remove(SHARED_PREF_USER_KEY)

        // Execute function to be tested
        classUnderTest.clearUserFromCache()

        // Verify behaviour
        verify(classUnderTest).clearUserFromCache() // Called by this test
        verify(sharedPrefUtils).remove(SHARED_PREF_USER_KEY)
        verifyNoMoreInteractions()
    }

    /** Method: [UserUseCaseImpl.getUserFromCache] */
    @Test
    fun getUserFromCache() {
        val expectedUser = UserFactory.user()
        val expectedUserJson = gson.toJson(expectedUser)

        // Mock behaviour
        `when`(sharedPrefUtils.getString(SHARED_PREF_USER_KEY, null))
            .thenReturn(expectedUserJson)

        // Execute function to be tested
        val actualUser = classUnderTest.getUserFromCache()

        // Assertions
        assertThat(actualUser).isNotNull
        assertEqual(expectedUser, actualUser!!)

        // Verify behaviour
        verify(classUnderTest).getUserFromCache() // Called by this test
        verify(sharedPrefUtils).getString(SHARED_PREF_USER_KEY, null)
        verifyNoMoreInteractions()
    }

    /** Method: [UserUseCaseImpl.saveUserToCache] */
    @Test
    fun saveUserToCache() {
        val expectedUser = UserFactory.user()
        val expectedUserJson = gson.toJson(expectedUser)

        // Mock behaviour
        doNothing().`when`(sharedPrefUtils).setString(SHARED_PREF_USER_KEY, expectedUserJson)

        // Execute function to be tested
        classUnderTest.saveUserToCache(expectedUser)

        // Verify behaviour
        verify(classUnderTest).saveUserToCache(expectedUser) // Called by this test
        verify(sharedPrefUtils).setString(SHARED_PREF_USER_KEY, expectedUserJson)
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
        val user = UserFactory.user()
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
        val expectedUser = UserFactory.user()
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

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(mockUserApi, sharedPrefUtils, userApi, userDao)
    }
}
