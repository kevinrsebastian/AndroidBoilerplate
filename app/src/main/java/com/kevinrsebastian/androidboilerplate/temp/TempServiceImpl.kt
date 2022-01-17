package com.kevinrsebastian.androidboilerplate.temp

import javax.inject.Inject

/* No tests.
 * Just a temp class to demo @Bind annotation for Hilt DI.
 */
class TempServiceImpl @Inject constructor(
    private val tempGreeter: TempGreeter
) : TempService {

    override fun getGreeting(): String {
        return tempGreeter.greeting()
    }

    override fun setGreetingSubject(subject: String) {
        tempGreeter.setSubject(subject)
    }
}
