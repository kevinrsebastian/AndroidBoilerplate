package com.fivenapp.user.model.usecase

import com.fivenapp.user.model.data.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserUseCase {

    // =================================================================================================================
    // API functions
    // =================================================================================================================

    fun getUsersFromApi(): Single<List<User>>

    fun getUserFromApi(id: String): Single<User>

    // =================================================================================================================
    // DB functions
    // =================================================================================================================

    fun addUserToDb(user: User): Completable

    fun getUserFromDb(id: String): Single<User>

    fun getUsersFromDb(): Single<List<User>>
}
