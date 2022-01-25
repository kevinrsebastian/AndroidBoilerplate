package com.kevinrsebastian.androidboilerplate.model.usecase

import com.kevinrsebastian.androidboilerplate.api.MockApi
import com.kevinrsebastian.androidboilerplate.model.data.User
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class UserUseCaseImpl @Inject constructor(
    private val mockApi: MockApi
) : UserUseCase {

    override fun getUsersFromApi(): Single<List<User>> {
        return mockApi.getAllUsers()
    }

    override fun getUserFromApi(id: String): Single<User> {
        return mockApi.getUser(id)
    }
}
