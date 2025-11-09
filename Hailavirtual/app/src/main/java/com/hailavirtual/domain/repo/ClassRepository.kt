package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.SchoolClass

interface ClassRepository {
    suspend fun createClass(
        schoolId: String,
        teacherId: String,
        name: String
    ): SchoolClass

    suspend fun assignTeacher(
        classId: String,
        teacherId: String
    )

    // optional
    suspend fun deleteClass(classId: String)

    suspend fun getClassById(classId: String): SchoolClass?
}