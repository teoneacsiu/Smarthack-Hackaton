package com.hailavirtual.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.hailavirtual.data.mappers.toUser
import com.hailavirtual.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose

class UserDataSource(
    private val db: FirebaseFirestore
) {
    fun observeUser(userId: String): Flow<User?> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toUser())
            }
        awaitClose { listener.remove() }
    }

    suspend fun setUser(user: User) {
        db.collection("users")
            .document(user.id)
            .set(
                mapOf(
                    "fullName" to user.fullName,
                    "email" to user.email,
                    "role" to user.role.name,
                    "schoolId" to user.schoolId
                )
            )
            .await()
    }
}
