package ph.teko.app.user_model.api

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ph.teko.app.test.assertion.UserAssertion
import ph.teko.app.test.base.BaseUnitTest
import ph.teko.app.test.factory.UserFactory

@RunWith(JUnit4::class)
internal class UserResponseTest : BaseUnitTest() {

    /** Method: [UserResponse] */
    @Test
    fun toUser() {
        val expectedUser = UserFactory.user()
        val userResponse = UserResponse(
            uid = expectedUser.id,
            fname = expectedUser.firstName,
            lname = expectedUser.lastName
        )
        val actualUser = userResponse.toUser()

        UserAssertion.assertEqual(actualUser, expectedUser)
    }
}
