package ph.teko.app.core.di

import javax.inject.Qualifier

object HiltQualifiers {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TekoRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MockRetrofit
}
