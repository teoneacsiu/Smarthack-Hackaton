package com.hailavirtual.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.hailavirtual.data.model.Experiment
import com.hailavirtual.domain.repo.ExperimentRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExperimentRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ExperimentRepository {

    override suspend fun createExperiment(
        lessonId: String,
        name: String,
        substanceIds: List<String>,
        equipmentIds: List<String>,
        recipeJson: String,
        resultJson: String
    ): Experiment {
        val doc = db.collection("experiments").document()

        val experiment = Experiment(
            id = doc.id,
            lessonId = lessonId,
            name = name,
            substanceIds = substanceIds,
            equipmentIds = equipmentIds,
            recipeJson = recipeJson,
            resultJson = resultJson
        )

        // 1) scriem experimentul
        doc.set(
            mapOf(
                "lessonId" to experiment.lessonId,
                "name" to experiment.name,
                "substanceIds" to experiment.substanceIds,
                "equipmentIds" to experiment.equipmentIds,
                "recipeJson" to experiment.recipeJson,
                "resultJson" to experiment.resultJson
            )
        ).await()

        // 2) il atasam la lectie (lesson.experimentIds)
        db.collection("lessons")
            .document(lessonId)
            .update("experimentIds", FieldValue.arrayUnion(experiment.id))
            .await()

        return experiment
    }

    override suspend fun updateExperiment(
        experimentId: String,
        name: String?,
        substanceIds: List<String>?,
        equipmentIds: List<String>?,
        recipeJson: String?,
        resultJson: String?
    ) {
        val updates = mutableMapOf<String, Any>()

        if (name != null) updates["name"] = name
        if (substanceIds != null) updates["substanceIds"] = substanceIds
        if (equipmentIds != null) updates["equipmentIds"] = equipmentIds
        if (recipeJson != null) updates["recipeJson"] = recipeJson
        if (resultJson != null) updates["resultJson"] = resultJson

        if (updates.isNotEmpty()) {
            db.collection("experiments")
                .document(experimentId)
                .update(updates)
                .await()
        }
    }

    override suspend fun deleteExperiment(experimentId: String) {
        // optional: citesti experimentul ca sa stii lessonId si il scoti din lesson.experimentIds
        val snap = db.collection("experiments")
            .document(experimentId)
            .get()
            .await()

        val lessonId = snap.getString("lessonId")

        db.collection("experiments")
            .document(experimentId)
            .delete()
            .await()

        if (lessonId != null) {
            db.collection("lessons")
                .document(lessonId)
                .update("experimentIds", FieldValue.arrayRemove(experimentId))
                .await()
        }
    }
}