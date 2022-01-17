package com.kevinrsebastian.androidboilerplate.temp

import javax.inject.Inject

/* No tests.
 * Just a temp class to demo @Provides annotation for Hilt DI.
 */
class TempGreeterImpl @Inject constructor(
    private var subject: String
): TempGreeter {

    override fun greeting(): String {
        return "Hello $subject!"
    }

    override fun setSubject(subject: String) {
        this.subject = subject
    }
}
