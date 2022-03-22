package com.fivenapp.user.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val firstName: String,
    val lastName: String
) : Parcelable
