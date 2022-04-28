package ph.teko.app.user_model.usecase

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ph.teko.app.core.preferences.SharedPrefUtils
import ph.teko.app.user_model.api.MockUserApi
import ph.teko.app.user_model.api.UserApi
import ph.teko.app.user_model.constants.SHARED_PREF_USER_KEY
import ph.teko.app.user_model.data.User
import ph.teko.app.user_model.db.UserDao
import ph.teko.app.user_model.db.UserEntity
import javax.inject.Inject

internal class UserUseCaseImpl @Inject constructor(
    private val mockUserApi: MockUserApi,
    private val sharedPrefUtils: SharedPrefUtils,
    private val userApi: UserApi,
    private val userDao: UserDao
) : UserUseCase {

    private val gson = Gson()

    // =================================================================================================================
    // API functions
    // =================================================================================================================

    override fun getUsersFromApi(): Single<List<User>> {
        return mockUserApi.getAllUsers()
    }

    override fun getUserFromApi(id: String): Single<User> {
        return mockUserApi.getUser(id)
    }

    override fun login(email: String, password: String): Single<User> {
        return userApi.login(email, password)
            .flatMap { response ->
                Single.fromCallable {
                    val user = response.body()!!.toUser()
                    saveUserToCache(user)
                    user
                }
            }
    }

    override fun checkLogin(): Single<User> {
        return userApi.checkLogin()
            .flatMap { response ->
                Single.fromCallable {
                    response.body()!!.toUser()
                }
            }
    }

    // =================================================================================================================
    // Cache functions
    // =================================================================================================================
    override fun clearUserFromCache() {
        sharedPrefUtils.remove(SHARED_PREF_USER_KEY)
    }

    override fun getUserFromCache(): User? {
        return try {
            val userJson = sharedPrefUtils.getString(SHARED_PREF_USER_KEY, null)
            if (userJson.isNullOrBlank()) {
                null
            } else {
                gson.fromJson(userJson, User::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun saveUserToCache(user: User) {
        return sharedPrefUtils.setString(SHARED_PREF_USER_KEY, gson.toJson(user))
    }

    // =================================================================================================================
    // DB functions
    // =================================================================================================================

    override fun addUserToDb(user: User): Completable {
        return try {
            userDao.insertUser(UserEntity(user.id.toInt(), user.firstName, user.lastName))
        } catch (e: NumberFormatException) {
            Completable.error(e)
        }
    }

    override fun getUsersFromDb(): Single<List<User>> {
        return userDao.getUsers()
            .flatMapObservable { userEntityList ->
                Observable.fromIterable(userEntityList)
            }
            .flatMap { userEntity ->
                Observable.fromCallable { User(userEntity.id.toString(), userEntity.firstName, userEntity.lastName) }
            }
            .toList()
    }

    override fun getUserFromDb(id: String): Single<User> {
        return try {
            userDao.getUser(id.toInt())
                .map { userEntity ->
                    User(userEntity.id.toString(), userEntity.firstName, userEntity.lastName)
                }
        } catch (e: NumberFormatException) {
            Single.error(e)
        }
    }
}
