package com.kevinrsebastian.user.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kevinrsebastian.user.model.data.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(
    UserEntity.TypeConverter::class
)
internal abstract class UserDb : RoomDatabase() {

    companion object {
        @JvmStatic
        fun create(context: Context): UserDb {
            return Room.databaseBuilder(context, UserDb::class.java, "local-cache.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun userDao(): UserDao
}
