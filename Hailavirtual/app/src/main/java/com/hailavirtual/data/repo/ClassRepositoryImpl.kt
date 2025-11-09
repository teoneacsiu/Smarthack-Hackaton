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
        // 1) încercăm mai întâi după id de document, cum era
        val docSnap = db.collection("classes").document(classId).get().await()
        if (docSnap.exists()) {
            return SchoolClass(
                id = docSnap.id,
                schoolId = docSnap.getString("schoolId").orElse(),
                teacherId = docSnap.getString("teacherId").orElse(),
                name = docSnap.getString("name").orElse(),
                lessonIds = (docSnap.get("lessonIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
            )
        }

        // 2) dacă nu există document cu id-ul ăsta, încercăm să-l găsim după field-ul "id"
        val querySnap = db.collection("classes")
            .whereEqualTo("id", classId)
            .limit(1)
            .get()
            .await()

        val doc = querySnap.documents.firstOrNull() ?: return null

        return SchoolClass(
            id = doc.id,
            schoolId = doc.getString("schoolId").orElse(),
            teacherId = doc.getString("teacherId").orElse(),
            name = doc.getString("name").orElse(),
            lessonIds = (doc.get("lessonIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
        )
    }

}

private fun String?._orEmpty() = this ?: ""
private fun String?.orElse(fallback: String = "") = this ?: fallback