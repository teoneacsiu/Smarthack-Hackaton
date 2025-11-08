package com.hailavirtual.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.hailavirtual.data.model.User
import com.hailavirtual.data.model.UserRole

fun DocumentSnapshot.toUser(): User? {
    if (!exists()) return null
    val fullName = getString("fullName").orEmpty()
    val email = getString("email").orEmpty()
    val roleStr = getString("role") ?: return null

    val role = UserRole.valueOf(roleStr)
    val schoolId = getString("schoolId")

    return User(
        id = id,            // id-ul documentului == uid user
        fullName = fullName,
        email = email,
        role = role,
        schoolId = schoolId
    )
}