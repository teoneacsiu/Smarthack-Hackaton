package com.hailavirtual.data.model

data class Teacher(
    val id: String = "",
    val schoolId: String = "",
    val name: String = "",
    val classIds: List<String> = emptyList()
)