package com.hailavirtual.data.model

data class School(
    val id: String = "",
    val name: String = "",
    val teacherIds: List<String> = emptyList(),
    val createdBy: String = ""
)