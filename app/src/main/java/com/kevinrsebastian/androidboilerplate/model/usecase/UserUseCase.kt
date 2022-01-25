package com.kevinrsebastian.androidboilerplate.model.usecase

import com.kevinrsebastian.androidboilerplate.model.data.User
import io.reactivex.rxjava3.core.Single

interface UserUseCase {

    fun getUsersFromApi(): Single<List<User>>

    fun getUserFromApi(id: String): Single<User>
}
