package com.hailavirtual.data.model

data class Experiment(
    val id: String = "",
    val lessonId: String = "",                // legatura directa cu lectia
    val name: String = "",
    val substanceIds: List<String> = emptyList(),
    val equipmentIds: List<String> = emptyList(),
    val recipeJson: String = "",              // JSON string
    val resultJson: String = ""               // JSON string
)