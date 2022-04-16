package ph.teko.app.user_model.api

import io.reactivex.rxjava3.core.Single
import ph.teko.app.user_model.data.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

// No Tests
internal interface UserApi {

    @FormUrlEncoded
    @GET("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("isSpecial") isSpecial: Boolean = true
    ): Single<UserResponse>

    @GET("users/login")
    fun checkLogin(): Single<User>
}
