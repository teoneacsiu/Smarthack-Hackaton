package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.Experiment
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

    suspend fun getLessonsByClassId(classId: String): List<Lesson>

    suspend fun getLessonById(id: String): Lesson?
    suspend fun getExperimentById(id: String): Experiment?
}