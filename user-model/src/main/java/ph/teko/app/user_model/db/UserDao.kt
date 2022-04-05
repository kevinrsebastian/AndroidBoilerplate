package ph.teko.app.user_model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/* No tests for this class. Instrumentation tests for DAOs are not working in android feature modules. */
// TODO: Find a way to run DAO tests as unit tests
@Dao
internal abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: UserEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users: List<UserEntity>): Completable

    @Query("SELECT * FROM user WHERE id = :userId")
    abstract fun getUser(userId: Int): Single<UserEntity>

    @Query("SELECT * FROM user")
    abstract fun getUsers(): Single<List<UserEntity>>

    @Query("DELETE FROM user WHERE id = :userId")
    abstract fun deleteUser(userId: Int): Completable

    @Query("DELETE FROM user")
    abstract fun clearUsers(): Completable
}
