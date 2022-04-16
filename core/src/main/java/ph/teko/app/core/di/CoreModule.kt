package ph.teko.app.core.di

import android.content.Context
import android.os.Environment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ph.teko.app.core.BuildConfig
import ph.teko.app.core.config.EnvConfig
import ph.teko.app.core.di.HiltQualifiers.MockRetrofit
import ph.teko.app.core.di.HiltQualifiers.TekoRetrofit
import ph.teko.app.core.preferences.SharedPrefUtils
import ph.teko.app.core.preferences.SharedPrefUtilsImpl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

/* Not set as internal because test hilt modules in this project module are not being detected during instrumentation.
 * The test module replacing this is moved to the app module. */
@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    internal fun provideSharedPrefUtils(@ApplicationContext context: Context): SharedPrefUtils {
        return SharedPrefUtilsImpl(context)
    }

    @Provides @MockRetrofit
    @Singleton
    internal fun provideMockRetrofit(@ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://61e80425e32cd90017acbf6f.mockapi.io/")
            .client(createOkHttpClient(context, "mock-okhttp"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides @TekoRetrofit
    @Singleton
    internal fun provideTekoRetrofit(@ApplicationContext context: Context, envConfig: EnvConfig): Retrofit {
        return Retrofit.Builder()
            .baseUrl(envConfig.tekoRestApiUrl)
            .client(createOkHttpClient(context, "teko-okhttp"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun createOkHttpClient(context: Context, cacheFileName: String): OkHttpClient {
        return OkHttpClient.Builder().apply {
            // Cache size
            val isWritable = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            val cacheDir = if (isWritable) context.externalCacheDir else context.cacheDir
            val diskSize = 250 * 1024 * 1024
            cache(Cache(File(cacheDir, cacheFileName), diskSize.toLong()))

            // Add header interceptor
            val headerInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val request = originalRequest.newBuilder().build()
                chain.proceed(request)
            }
            addInterceptor(headerInterceptor)

            // Debug interceptors
            if (BuildConfig.DEBUG) {
                // Logging
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }.build()
    }
}
