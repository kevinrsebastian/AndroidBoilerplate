package com.kevinrsebastian.androidboilerplate.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevinrsebastian.androidboilerplate.model.data.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/* No tests for this class */
@Dao
internal abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: UserEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users: List<UserEntity>): Completable

    @Query("SELECT * FROM ${DbTable.USER} WHERE id = :userId")
    abstract fun getUser(userId: Int): Single<UserEntity>

    @Query("SELECT * FROM ${DbTable.USER}")
    abstract fun getUsers(): Single<List<UserEntity>>

    @Query("DELETE FROM ${DbTable.USER} WHERE id = :userId")
    abstract fun deleteUser(userId: Int): Completable

    @Query("DELETE FROM ${DbTable.USER}")
    abstract fun clearUsers(): Completable
}
