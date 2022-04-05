package ph.teko.app.test.factory

import org.fluttercode.datafactory.impl.DataFactory
import ph.teko.app.user_model.data.User

object UserFactory {

    private val dataFactory = DataFactory()

    fun user(id: String? = null): User {
        return if (id != null)
            User(id, dataFactory.firstName, dataFactory.lastName)
        else
            User(dataFactory.getNumberUpTo(999).toString(), dataFactory.firstName, dataFactory.lastName)
    }
}
