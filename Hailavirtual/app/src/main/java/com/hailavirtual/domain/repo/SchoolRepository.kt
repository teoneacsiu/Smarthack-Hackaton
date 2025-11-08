package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.School

interface SchoolRepository {
    suspend fun createSchoolForUser(
        schoolUserId: String,
        name: String
    ): School
}