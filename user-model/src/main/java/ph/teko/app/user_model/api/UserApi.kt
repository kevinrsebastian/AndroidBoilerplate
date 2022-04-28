package ph.teko.app.user_model.api

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

// No Tests
internal interface UserApi {

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("isSpecial") isSpecial: Boolean = true
    ): Single<Response<UserResponse>>

    @GET("users/login")
    fun checkLogin(): Single<Response<UserResponse>>
}
