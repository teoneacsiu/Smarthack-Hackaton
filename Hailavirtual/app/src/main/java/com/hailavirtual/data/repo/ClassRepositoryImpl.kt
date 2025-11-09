package com.hailavirtual.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.hailavirtual.data.model.SchoolClass
import com.hailavirtual.domain.repo.ClassRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ClassRepository {

    override suspend fun createClass(
        schoolId: String,
        teacherId: String,
        name: String
    ): SchoolClass {
        val doc = db.collection("classes").document()

        val clazz = SchoolClass(
            id = doc.id,
            schoolId = schoolId,
            teacherId = teacherId,
            name = name,
            lessonIds = emptyList()
        )

        doc.set(
            mapOf(
                "schoolId" to clazz.schoolId,
                "teacherId" to clazz.teacherId,
                "name" to clazz.name,
                "lessonIds" to clazz.lessonIds
            )
        ).await()

        // optional: adaugi clasa in lista de classIds a profesorului
        db.collection("teachers")
            .document(teacherId)
            .update("classIds", FieldValue.arrayUnion(clazz.id))
            .await()

        return clazz
    }

    override suspend fun assignTeacher(
        classId: String,
        teacherId: String
    ) {
        db.collection("classes")
            .document(classId)
            .update("teacherId", teacherId)
            .await()

        // optional: update si pe teachers.classIds
        db.collection("teachers")
            .document(teacherId)
            .update("classIds", FieldValue.arrayUnion(classId))
            .await()
    }

    override suspend fun deleteClass(classId: String) {
        db.collection("classes")
            .document(classId)
            .delete()
            .await()
    }

    override suspend fun getClassById(classId: String): SchoolClass? {
        val snap = db.collection("classes").document(classId).get().await()
        if (!snap.exists()) return null

        return SchoolClass(
            id = snap.id,
            schoolId = snap.getString("schoolId").orElse(),
            teacherId = snap.getString("teacherId").orElse(),
            name = snap.getString("name").orElse(),
            lessonIds = (snap.get("lessonIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
        )
    }
}

private fun String?._orEmpty() = this ?: ""
private fun String?.orElse(fallback: String = "") = this ?: fallback