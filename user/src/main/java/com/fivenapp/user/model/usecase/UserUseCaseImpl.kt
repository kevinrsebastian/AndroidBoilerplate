package com.fivenapp.user.model.usecase

import com.fivenapp.user.model.api.MockUserApi
import com.fivenapp.user.model.data.User
import com.fivenapp.user.model.data.UserEntity
import com.fivenapp.user.model.db.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class UserUseCaseImpl @Inject constructor(
    private val mockUserApi: MockUserApi,
    private val userDao: UserDao
) : UserUseCase {

    // =================================================================================================================
    // API functions
    // =================================================================================================================

    override fun getUsersFromApi(): Single<List<User>> {
        return mockUserApi.getAllUsers()
    }

    override fun getUserFromApi(id: String): Single<User> {
        return mockUserApi.getUser(id)
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
