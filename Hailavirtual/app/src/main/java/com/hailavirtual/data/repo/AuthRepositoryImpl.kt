package com.hailavirtual.data.repo

import com.hailavirtual.domain.repo.AuthRepository
import com.hailavirtual.data.firebase.AuthDataSource
import com.hailavirtual.data.firebase.UserDataSource
import kotlinx.coroutines.flow.Flow
import com.hailavirtual.data.model.User
import com.hailavirtual.data.model.UserRole
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/** Repository facade over AuthDataSource.
 * Converts Firebase callbacks into suspend/Flow APIs for ViewModels. */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: AuthDataSource,
    private val users: UserDataSource,
    private val teacherRepository: TeacherRepository
) : AuthRepository {

    // AuthState (Firebase) + profil din Firestore
    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentUser: Flow<User?> =
        auth.currentUser.flatMapLatest { firebaseUser ->
            if (firebaseUser == null) flowOf(null)
            else users.observeUser(firebaseUser.uid)
        }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User> = runCatching {
        val fbUser = auth.signInEmail(email, password)
        val user = users.getUserOnce(fbUser.uid)
            ?: throw IllegalStateException("User profile not found in Firestore for uid=${fbUser.uid}")
        user
    }

    override suspend fun signUpSchool(
        schoolName: String,
        email: String,
        password: String
    ): Result<User> = runCatching {
        val fbUser = auth.signUpEmail(schoolName, email, password)

        val user = User(
            id = fbUser.uid,
            fullName = schoolName,
            email = email,
            role = UserRole.SCHOOL,
            schoolId = fbUser.uid   // scoala se identifica cu propriul uid
        )

        users.setUser(user)
        user
    }

    override suspend fun createTeacherAccount(
        schoolId: String,
        fullName: String,
        email: String,
        password: String
    ): Result<User> = runCatching {
        val newUserId = createUserBackend(email, password)

        val user = User(
            id = newUserId,
            fullName = fullName,
            email = email,
            role = UserRole.TEACHER,
            schoolId = schoolId
        )

        users.setUser(user)

        // plus in teacher table
        teacherRepository.createTeacher(
            teacherId = newUserId,
            schoolId = schoolId,
            name = fullName
        )

        user
    }

    // --- dummy backend impl, just for demo ---
    private fun createUserBackend(
        email: String,
        password: String
    ): String {
        // aici ai putea loga, trimite analytics etc.
        // pentru demo e doar un UID random
        return java.util.UUID.randomUUID().toString()
    }

    override suspend fun signOut() = auth.signOut()
}