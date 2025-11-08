package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.Lesson

interface LessonRepository {
    suspend fun createLesson(
        classId: String,
        teacherId: String,
        name: String,
        experimentIds: List<String>
    ): Lesson

    suspend fun updateLesson(
        lessonId: String,
        name: String? = null,
        experimentIds: List<String>? = null
    )

    suspend fun deleteLesson(lessonId: String)
}