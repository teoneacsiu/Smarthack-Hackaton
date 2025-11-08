package com.hailavirtual.domain.repo

import android.annotation.SuppressLint
import kotlinx.coroutines.flow.Flow
import com.hailavirtual.data.model.User

@SuppressLint("RestrictedApi")
interface AuthRepository {
    val currentUser: Flow<User?>

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun signUpSchool(
        schoolName: String,
        email: String,
        password: String
    ): Result<User>

    // apelat DOAR cand user-ul curent e SCHOOL sau ADMIN
    suspend fun createTeacherAccount(
        schoolId: String,
        fullName: String,
        email: String,
        password: String
    ): Result<User>

    suspend fun signOut()
}