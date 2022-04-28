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

    /**
     * Log in using email and password, saving the returned [User] details in SharedPreferences.
     * @param email the user's email
     * @param password the user's password
     * @return an RxSingle [User]
     */
    fun login(email: String, password: String): Single<User>

    /**
     * Check the logged-in remote user session, returning the logged-in [User].
     * @return an RxSingle [User]
     */
    fun checkLogin(): Single<User>

    // =================================================================================================================
    // Cache functions
    // =================================================================================================================

    /** Clear the [User] saved in local cache */
    fun clearUserFromCache()

    /**
     * Load the [User] saved in local cache
     * @return the cached [User], or null if there is no saved user
     */
    fun getUserFromCache(): User?

    /** Save [User] in the local cache */
    fun saveUserToCache(user: User)

    // =================================================================================================================
    // DB functions
    // =================================================================================================================

    fun addUserToDb(user: User): Completable

    fun getUserFromDb(id: String): Single<User>

    fun getUsersFromDb(): Single<List<User>>
}
