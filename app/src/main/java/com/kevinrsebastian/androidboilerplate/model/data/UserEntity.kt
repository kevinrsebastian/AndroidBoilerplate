package com.kevinrsebastian.androidboilerplate.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kevinrsebastian.androidboilerplate.model.db.DbTable

@Entity(tableName = DbTable.USER)
internal data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val firstName: String,
    val lastName: String
) {
    class TypeConverter {
        @androidx.room.TypeConverter
        fun listToString(list: List<UserEntity>): String {
            val type = object : TypeToken<List<UserEntity>>() { }.type
            return Gson().toJson(list, type)
        }
        @androidx.room.TypeConverter
        fun stringToList(json: String): List<UserEntity> {
            val type = object : TypeToken<List<UserEntity>>() { }.type
            return Gson().fromJson(json, type)
        }
    }
}
