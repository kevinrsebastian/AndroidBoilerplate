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
    fun toUser(): User {
        return User(id = this.uid, firstName = this.fname, lastName = this.lname)
    }
}
