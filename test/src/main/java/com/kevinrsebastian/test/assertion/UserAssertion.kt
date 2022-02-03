package com.kevinrsebastian.test.assertion

import com.kevinrsebastian.user.model.data.User
import org.assertj.core.api.Assertions.assertThat

object UserAssertion {

    fun assertEqual(user1: User, user2: User) {
        assertThat(user1.id).isEqualTo(user1.id)
        assertThat(user1.firstName).isEqualTo(user1.firstName)
        assertThat(user1.lastName).isEqualTo(user1.lastName)
    }

    fun assertEqual(users1: List<User>, users2: List<User>) {
        assertThat(users1).hasSameSizeAs(users2)
        for (i in users1.indices) {
            assertEqual(users1[i], users2[i])
        }
    }
}
