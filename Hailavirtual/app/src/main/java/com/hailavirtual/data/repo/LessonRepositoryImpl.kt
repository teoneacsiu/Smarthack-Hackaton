package com.hailavirtual.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.hailavirtual.data.model.Lesson
import com.hailavirtual.domain.repo.LessonRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : LessonRepository {

    override suspend fun createLesson(
        classId: String,
        teacherId: String,
        name: String,
        experimentIds: List<String>
    ): Lesson {
        val doc = db.collection("lessons").document()

        val lesson = Lesson(
            id = doc.id,
            classId = classId,
            teacherId = teacherId,
            name = name,
            experimentIds = experimentIds
        )

        doc.set(
            mapOf(
                "classId" to lesson.classId,
                "teacherId" to lesson.teacherId,
                "name" to lesson.name,
                "experimentIds" to lesson.experimentIds
            )
        ).await()

        // also attach lesson to the class (classes.lessonIds)
        db.collection("classes")
            .document(classId)
            .update("lessonIds", FieldValue.arrayUnion(lesson.id))
            .await()

        return lesson
    }

    override suspend fun updateLesson(
        lessonId: String,
        name: String?,
        experimentIds: List<String>?
    ) {
        val updates = mutableMapOf<String, Any>()
        if (name != null) updates["name"] = name
        if (experimentIds != null) updates["experimentIds"] = experimentIds

        if (updates.isNotEmpty()) {
            db.collection("lessons")
                .document(lessonId)
                .update(updates)
                .await()
        }
    }

    override suspend fun deleteLesson(lessonId: String) {
        // optional: remove id from class.lessonIds too (need classId for that)
        db.collection("lessons")
            .document(lessonId)
            .delete()
            .await()
    }
}