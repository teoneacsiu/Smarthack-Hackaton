package com.hailavirtual.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hailavirtual.data.model.School
import com.hailavirtual.domain.repo.SchoolRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SchoolRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SchoolRepository {

    override suspend fun createSchoolForUser(
        schoolUserId: String,
        name: String
    ): School {
        val currentUid = auth.currentUser?.uid
            ?: error("Not logged in")

        // optional: verifici si in UI ca currentUser.role == ADMIN

        val school = School(
            id = schoolUserId,
            name = name,
            teacherIds = emptyList(),
            createdBy = currentUid
        )

        db.collection("schools")
            .document(schoolUserId)  // id = uid user SCHOOL
            .set(school)
            .await()

        return school
    }
}