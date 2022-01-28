package com.kevinrsebastian.androidboilerplate.model.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinrsebastian.androidboilerplate.model.data.UserEntity
import org.assertj.core.api.Assertions
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/*
 * No need to verify behaviour since this will directly access the db, and all mocked/spied behaviour will be
 *   called by this test.
 */
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private val factory = DataFactory()

    private lateinit var userDao: UserDao

    private lateinit var db: AndroidBoilerplateDb

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AndroidBoilerplateDb::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    /**
     * Method: [UserDao.insertUser] and [UserDao.getUser]
     * Scenario: Insert a user into DB and check using [UserDao.getUser].
     */
    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() {
        val expectedUser = UserEntity(1, factory.firstName, factory.lastName)

        userDao.insertUser(expectedUser).blockingAwait()
        val actualUser = userDao.getUser(expectedUser.id).blockingGet()

        assertEqual(actualUser, expectedUser)
    }

    /**
     * Method: [UserDao.insertUsers] and [UserDao.getUsers]
     * Scenario: Insert users into DB and check using [UserDao.getUsers].
     */
    @Test
    @Throws(Exception::class)
    fun insertAndGetUsers() {
        val user1 = UserEntity(1, factory.firstName, factory.lastName)
        val user2 = UserEntity(2, factory.firstName, factory.lastName)
        val user3 = UserEntity(3, factory.firstName, factory.lastName)
        val expectedUsers = listOf(user1, user2, user3)

        userDao.insertUsers(expectedUsers).blockingAwait()
        val actualUsers = userDao.getUsers().blockingGet()

        assertEqual(actualUsers, expectedUsers)
    }

    /**
     * Method: [UserDao.deleteUser]
     * Scenario: Insert users into DB, delete one, and check using [UserDao.getUsers].
     */
    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        val user1 = UserEntity(1, factory.firstName, factory.lastName)
        val user2 = UserEntity(2, factory.firstName, factory.lastName)
        val user3 = UserEntity(3, factory.firstName, factory.lastName)
        val users = listOf(user1, user2, user3)
        val userIdToDelete = user2.id
        val expectedUsers = listOf(user1, user2, user3)

        userDao.insertUsers(users).blockingAwait()
        userDao.deleteUser(userIdToDelete)
        val actualUsers = userDao.getUsers().blockingGet()

        assertEqual(actualUsers, expectedUsers)
    }

    /**
     * Method: [UserDao.clearUsers]
     * Scenario: Insert users into DB, clear, and check using [UserDao.getUsers].
     */
    @Test
    @Throws(Exception::class)
    fun clearUsers() {
        val user1 = UserEntity(1, factory.firstName, factory.lastName)
        val user2 = UserEntity(2, factory.firstName, factory.lastName)
        val user3 = UserEntity(3, factory.firstName, factory.lastName)
        val users = listOf(user1, user2, user3)

        userDao.insertUsers(users).blockingAwait()
        userDao.clearUsers().blockingAwait()
        val actualUsers = userDao.getUsers().blockingGet()

        Assertions.assertThat(actualUsers).isEmpty()
    }

    private fun assertEqual(users1: List<UserEntity>, users2: List<UserEntity>) {
        Assertions.assertThat(users1).hasSameSizeAs(users2)
        for (i in users1.indices) {
            assertEqual(users1[i], users2[i])
        }
    }

    private fun assertEqual(user1: UserEntity, user2: UserEntity) {
        Assertions.assertThat(user1.id).isEqualTo(user2.id)
        Assertions.assertThat(user1.firstName).isEqualTo(user2.firstName)
        Assertions.assertThat(user1.lastName).isEqualTo(user2.lastName)
    }
}
