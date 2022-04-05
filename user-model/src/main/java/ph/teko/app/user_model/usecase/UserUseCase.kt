package ph.teko.app.user_model.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ph.teko.app.user_model.data.User

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
