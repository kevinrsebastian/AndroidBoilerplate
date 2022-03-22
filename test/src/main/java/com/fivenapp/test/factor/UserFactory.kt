package com.fivenapp.test.factor

import com.fivenapp.user.model.data.User
import org.fluttercode.datafactory.impl.DataFactory

object UserFactory {

    private val dataFactory = DataFactory()

    fun user(id: String? = null): User {
        return if (id != null)
            User(id, dataFactory.firstName, dataFactory.lastName)
        else
            User(dataFactory.getNumberUpTo(999).toString(), dataFactory.firstName, dataFactory.lastName)
    }
}
