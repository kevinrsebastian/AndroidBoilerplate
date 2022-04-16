package ph.teko.app.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
internal class SharedPrefUtilsImplTest {

    private val factory = DataFactory()
    private lateinit var classUnderTest: SharedPrefUtilsImpl
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val sharedPreferencesName = context.packageName + "_preferences"
        sharedPreferences = context.getSharedPreferences(
            sharedPreferencesName,
            Context.MODE_PRIVATE
        )
        classUnderTest = SharedPrefUtilsImpl(context)
    }

    /** Method: [SharedPrefUtilsImpl.clear] */
    @Test
    fun clear() {
        // Add dummy values
        val dummyKey1 = "dummyKey1"
        val dummyValue1 = factory.randomWord
        sharedPreferences.edit()
            .putString(dummyKey1, dummyValue1)
            .apply()
        val dummyKey2 = "dummyKey2"
        val dummyValue2 = factory.getNumberBetween(1000, 9999)
        sharedPreferences.edit()
            .putInt(dummyKey2, dummyValue2)
            .apply()

        // Assert that dummy values are saved
        assertThat(sharedPreferences.getString(dummyKey1, ""))
            .isEqualTo(dummyValue1)
        assertThat(sharedPreferences.getInt(dummyKey2, 0))
            .isEqualTo(dummyValue2)

        // Execute the function to be tested
        classUnderTest.clear()

        // Assert that the dummy values we saved are now gone
        assertThat(sharedPreferences.getString(dummyKey1, ""))
            .isBlank
        assertThat(sharedPreferences.getInt(dummyKey2, 0))
            .isEqualTo(0)
    }

    /** Method: [SharedPrefUtilsImpl.getBoolean] */
    @Test
    fun getBoolean() {
        // Test existing key-value pair
        val existingKey = "key1"
        val existingValue = factory.chance(50)
        // Add existing values
        sharedPreferences.edit()
            .putBoolean(existingKey, existingValue)
            .apply()
        // Assert values
        assertThat(classUnderTest.getBoolean(existingKey, !existingValue))
            .isEqualTo(existingValue)

        // Test non-existing key-value pair
        val nonExistingKey = "key2"
        val nonExistingFallbackValue = factory.chance(50)
        // Assert values
        assertThat(classUnderTest.getBoolean(nonExistingKey, nonExistingFallbackValue))
            .isEqualTo(nonExistingFallbackValue)
    }

    /** Method: [SharedPrefUtilsImpl.setBoolean] */
    @Test
    fun setBoolean() {
        val key = "key1"
        val expectedValue = factory.chance(50)

        // Set value
        classUnderTest.setBoolean(key, expectedValue)

        // Assert values
        assertThat(sharedPreferences.getBoolean(key, !expectedValue))
            .isEqualTo(expectedValue)
    }

    /** Method: [SharedPrefUtilsImpl.getFloat] */
    @Test
    fun getFloat() {
        // Test existing key-value pair
        val existingKey = "key1"
        val existingValue = 5.5f
        // Add existing values
        sharedPreferences.edit()
            .putFloat(existingKey, existingValue)
            .apply()
        // Assert values
        assertThat(classUnderTest.getFloat(existingKey, 0f))
            .isEqualTo(existingValue)

        // Test non-existing key-value pair
        val nonExistingKey = "key2"
        val nonExistingFallbackValue = 1.7f
        // Assert values
        assertThat(classUnderTest.getFloat(nonExistingKey, nonExistingFallbackValue))
            .isEqualTo(nonExistingFallbackValue)
    }

    /** Method: [SharedPrefUtilsImpl.setFloat] */
    @Test
    fun setFloat() {
        val key = "key1"
        val expectedValue = 8.7f

        // Set value
        classUnderTest.setFloat(key, expectedValue)

        // Assert values
        assertThat(sharedPreferences.getFloat(key, 0f))
            .isEqualTo(expectedValue)
    }

    /** Method: [SharedPrefUtilsImpl.getInt] */
    @Test
    fun getInt() {
        // Test existing key-value pair
        val existingKey = "key1"
        val existingValue = factory.getNumberBetween(1, 99)
        // Add existing values
        sharedPreferences.edit()
            .putInt(existingKey, existingValue)
            .apply()
        // Assert values
        assertThat(classUnderTest.getInt(existingKey, 0))
            .isEqualTo(existingValue)

        // Test non-existing key-value pair
        val nonExistingKey = "key2"
        val nonExistingFallbackValue = factory.getNumberBetween(1, 99)
        // Assert values
        assertThat(classUnderTest.getInt(nonExistingKey, nonExistingFallbackValue))
            .isEqualTo(nonExistingFallbackValue)
    }

    /** Method: [SharedPrefUtilsImpl.setInt] */
    @Test
    fun setInt() {
        val key = "key1"
        val expectedValue = factory.getNumberBetween(1, 99)

        // Set value
        classUnderTest.setInt(key, expectedValue)

        // Assert values
        assertThat(sharedPreferences.getInt(key, 0))
            .isEqualTo(expectedValue)
    }

    /** Method: [SharedPrefUtilsImpl.getLong] */
    @Test
    fun getLong() {
        // Test existing key-value pair
        val existingKey = "key1"
        val existingValue = factory.getNumberBetween(1, 99).toLong()
        // Add existing values
        sharedPreferences.edit()
            .putLong(existingKey, existingValue)
            .apply()
        // Assert values
        assertThat(classUnderTest.getLong(existingKey, 0L))
            .isEqualTo(existingValue)

        // Test non-existing key-value pair
        val nonExistingKey = "key2"
        val nonExistingFallbackValue = factory.getNumberBetween(1, 99).toLong()
        // Assert values
        assertThat(classUnderTest.getLong(nonExistingKey, nonExistingFallbackValue))
            .isEqualTo(nonExistingFallbackValue)
    }

    /** Method: [SharedPrefUtilsImpl.setLong] */
    @Test
    fun setLong() {
        val key = "key1"
        val expectedValue = factory.getNumberBetween(0, 99).toLong()

        // Set value
        classUnderTest.setLong(key, expectedValue)

        // Assert values
        assertThat(sharedPreferences.getLong(key, 0L))
            .isEqualTo(expectedValue)
    }

    /** Method: [SharedPrefUtilsImpl.getString] */
    @Test
    fun getString() {
        // Test existing key-value pair
        val existingKey = "key1"
        val existingValue = factory.randomWord
        // Add existing values
        sharedPreferences.edit()
            .putString(existingKey, existingValue)
            .apply()
        // Assert values
        assertThat(classUnderTest.getString(existingKey, factory.randomWord))
            .isEqualTo(existingValue)

        // Test non-existing key-value pair
        val nonExistingKey = "key2"
        val nonExistingFallbackValue = factory.randomWord
        // Assert values
        assertThat(classUnderTest.getString(nonExistingKey, nonExistingFallbackValue))
            .isEqualTo(nonExistingFallbackValue)
    }

    /** Method: [SharedPrefUtilsImpl.setString] */
    @Test
    fun setString() {
        val key = "key1"
        val expectedValue = factory.randomWord

        // Set value
        classUnderTest.setString(key, expectedValue)

        // Assert values
        assertThat(sharedPreferences.getString(key, factory.randomWord))
            .isEqualTo(expectedValue)
    }

    /** Method: [SharedPrefUtilsImpl.remove] */
    @Test
    fun remove() {
        // Add dummy values
        val dummyKey1 = "dummyKey1"
        val dummyValue1 = factory.randomWord
        sharedPreferences.edit()
            .putString(dummyKey1, dummyValue1)
            .apply()
        val dummyKey2 = "dummyKey2"
        val dummyValue2 = factory.getNumberBetween(1000, 9999)
        sharedPreferences.edit()
            .putInt(dummyKey2, dummyValue2)
            .apply()

        // Remove keys
        classUnderTest.remove(dummyKey1)
        classUnderTest.remove(dummyKey2)

        // Assert that the dummy values we saved are now gone
        assertThat(sharedPreferences.getString(dummyKey1, ""))
            .isBlank
        assertThat(sharedPreferences.getInt(dummyKey2, 0))
            .isEqualTo(0)
    }
}
