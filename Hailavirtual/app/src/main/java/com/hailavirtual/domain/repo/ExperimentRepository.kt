package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.Experiment

interface ExperimentRepository {
    suspend fun createExperiment(
        lessonId: String,
        name: String,
        substanceIds: List<String>,
        equipmentIds: List<String>,
        recipeJson: String,
        resultJson: String
    ): Experiment

    suspend fun updateExperiment(
        experimentId: String,
        name: String? = null,
        substanceIds: List<String>? = null,
        equipmentIds: List<String>? = null,
        recipeJson: String? = null,
        resultJson: String? = null
    )

    suspend fun deleteExperiment(experimentId: String)
}