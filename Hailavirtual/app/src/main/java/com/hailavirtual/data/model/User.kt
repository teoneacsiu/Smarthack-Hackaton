package com.hailavirtual.data.model

enum class UserRole {
    ADMIN,
    SCHOOL,
    TEACHER
}

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val schoolId: String? = null  // pentru TEACHER -> id-ul scolii
)