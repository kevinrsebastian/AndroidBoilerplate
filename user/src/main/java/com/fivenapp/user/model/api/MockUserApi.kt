package com.fivenapp.user.model.api

import com.fivenapp.user.model.data.User
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

/* Encases API endpoints for MockAPI: https://mockapi.io */
internal interface MockUserApi {

    // #################################################################################################################
    // User Endpoints
    // #################################################################################################################

    @GET("mock/user")
    fun getAllUsers(): Single<List<User>>

    @GET("mock/user/{id}")
    fun getUser(@Path("id") id: String): Single<User>
}
