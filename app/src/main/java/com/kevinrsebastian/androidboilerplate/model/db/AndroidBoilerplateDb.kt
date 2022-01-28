package com.kevinrsebastian.androidboilerplate.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kevinrsebastian.androidboilerplate.model.data.UserEntity

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
internal abstract class AndroidBoilerplateDb : RoomDatabase() {

    companion object {
        @JvmStatic
        fun create(context: Context): AndroidBoilerplateDb {
            return Room.databaseBuilder(context, AndroidBoilerplateDb::class.java, "local-cache.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun userDao(): UserDao
}
