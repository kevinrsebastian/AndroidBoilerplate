package com.kevinrsebastian.androidboilerplate.temp

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface TempService {

    fun getGreeting(): Single<String>

    fun setGreetingSubject(subject: String): Completable
}
