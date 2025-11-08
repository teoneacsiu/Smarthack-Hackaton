package com.hailavirtual.data.model

data class SchoolClass(
    val id: String = "",
    val schoolId: String = "",       // scoala de care apartine clasa
    val teacherId: String = "",      // profesorul titular
    val name: String = "",           // ex: "7A", "12B - Chimie"
    val lessonIds: List<String> = emptyList()
)