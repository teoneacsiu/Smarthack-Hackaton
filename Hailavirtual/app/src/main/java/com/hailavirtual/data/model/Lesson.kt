package com.hailavirtual.data.model

data class Lesson(
    val id: String = "",
    val classId: String = "",              // link to SchoolClass
    val teacherId: String = "",            // who owns / created it
    val name: String = "",
    val experimentIds: List<String> = emptyList()
)