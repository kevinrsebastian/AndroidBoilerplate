package com.kevinrsebastian.androidboilerplate.temp

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/* No tests.
 * Just a temp class to demo @Bind annotation for Hilt DI.
 */
class TempServiceImpl @Inject constructor(
    private val tempGreeter: TempGreeter
) : TempService {

    override fun getGreeting(): Single<String> {
        return Single.fromCallable { tempGreeter.greeting() }
    }

    override fun setGreetingSubject(subject: String): Completable {
        return Completable.fromAction {
            tempGreeter.setSubject(subject)
        }
    }
}
