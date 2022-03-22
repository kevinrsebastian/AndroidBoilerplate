package com.fivenapp.test.assertion

import com.fivenapp.user.model.data.User
import org.assertj.core.api.Assertions.assertThat

object UserAssertion {

    fun assertEqual(user1: User, user2: User) {
        assertThat(user1.id).isEqualTo(user2.id)
        assertThat(user1.firstName).isEqualTo(user2.firstName)
        assertThat(user1.lastName).isEqualTo(user2.lastName)
    }

    fun assertEqual(users1: List<User>, users2: List<User>) {
        assertThat(users1).hasSameSizeAs(users2)
        for (i in users1.indices) {
            assertEqual(users1[i], users2[i])
        }
    }
}
