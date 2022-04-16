package ph.teko.app.core.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

internal class SharedPrefUtilsImpl @Inject internal constructor(context: Context) : SharedPrefUtils {

    private val sharedPreferences: SharedPreferences

    init {
        val sharedPreferencesName = context.packageName + "_preferences"
        this.sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    override fun clear() = sharedPreferences.edit().clear().apply()

    override fun getBoolean(key: String, fallbackValue: Boolean) = sharedPreferences.getBoolean(key, fallbackValue)
    override fun setBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

    override fun getFloat(key: String, fallbackValue: Float) = sharedPreferences.getFloat(key, fallbackValue)
    override fun setFloat(key: String, value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

    override fun getInt(key: String, fallbackValue: Int) = sharedPreferences.getInt(key, fallbackValue)
    override fun setInt(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()

    override fun getLong(key: String, fallbackValue: Long) = sharedPreferences.getLong(key, fallbackValue)
    override fun setLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()

    override fun getString(key: String, fallbackValue: String?) = sharedPreferences.getString(key, fallbackValue)
    override fun setString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()

    override fun remove(key: String) = sharedPreferences.edit().remove(key).apply()
}
