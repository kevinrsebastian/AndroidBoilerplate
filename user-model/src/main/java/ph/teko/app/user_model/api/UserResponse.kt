package ph.teko.app.user_model.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ph.teko.app.user_model.data.User

@Parcelize
internal data class UserResponse(
    val uid: String,
    val fname: String,
    val lname: String
) : Parcelable {
    companion object {
        fun fromUser(user: User): UserResponse {
            return UserResponse(user.id, user.firstName, user.lastName)
        }
    }

    fun toUser(): User {
        return User(id = this.uid, firstName = this.fname, lastName = this.lname)
    }
}
