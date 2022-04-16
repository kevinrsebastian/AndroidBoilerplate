package ph.teko.app.core.preferences

/** Utility class for accessing SharedPreferences */
interface SharedPrefUtils {
    /** Clear all cached values. **/
    fun clear()

    fun getBoolean(key: String, fallbackValue: Boolean): Boolean
    fun setBoolean(key: String, value: Boolean)

    fun getFloat(key: String, fallbackValue: Float): Float
    fun setFloat(key: String, value: Float)

    fun getInt(key: String, fallbackValue: Int): Int
    fun setInt(key: String, value: Int)

    fun getLong(key: String, fallbackValue: Long): Long
    fun setLong(key: String, value: Long)

    fun getString(key: String, fallbackValue: String?): String?
    fun setString(key: String, value: String)

    fun remove(key: String)
}
