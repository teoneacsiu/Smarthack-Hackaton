package com.hailavirtual.data.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hailavirtual.data.model.Teacher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeacherRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun createTeacher(
        teacherId: String,
        schoolId: String,
        name: String
    ): Teacher {
        val teacher = Teacher(
            id = teacherId,
            schoolId = schoolId,
            name = name,
            classIds = emptyList()
        )

        // 1) scriem in teachers
        db.collection("teachers")
            .document(teacherId)
            .set(teacher)
            .await()

        // 2) adaugam id-ul in lista de teacherIds din school
        db.collection("schools")
            .document(schoolId)
            .update("teacherIds", FieldValue.arrayUnion(teacherId))
            .await()

        return teacher
    }
}