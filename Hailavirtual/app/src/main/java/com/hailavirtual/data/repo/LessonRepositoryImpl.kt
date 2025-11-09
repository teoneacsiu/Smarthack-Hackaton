package com.hailavirtual.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Experiment
import com.hailavirtual.data.model.Lesson
import com.hailavirtual.data.model.Substance
import com.hailavirtual.domain.repo.LessonRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : LessonRepository {

    override suspend fun createLesson(
        classId: String,
        teacherId: String,
        name: String,
        experimentIds: List<String>
    ): Lesson {
        val doc = db.collection("lessons").document()

        val lesson = Lesson(
            id = doc.id,
            classId = classId,
            teacherId = teacherId,
            name = name,
            experimentIds = experimentIds
        )

        doc.set(
            mapOf(
                "classId" to lesson.classId,
                "teacherId" to lesson.teacherId,
                "name" to lesson.name,
                "experimentIds" to lesson.experimentIds
            )
        ).await()

        // also attach lesson to the class (classes.lessonIds)
        db.collection("classes")
            .document(classId)
            .update("lessonIds", FieldValue.arrayUnion(lesson.id))
            .await()

        return lesson
    }

    override suspend fun updateLesson(
        lessonId: String,
        name: String?,
        experimentIds: List<String>?
    ) {
        val updates = mutableMapOf<String, Any>()
        if (name != null) updates["name"] = name
        if (experimentIds != null) updates["experimentIds"] = experimentIds

        if (updates.isNotEmpty()) {
            db.collection("lessons")
                .document(lessonId)
                .update(updates)
                .await()
        }
    }

    override suspend fun deleteLesson(lessonId: String) {
        // optional: remove id from class.lessonIds too (need classId for that)
        db.collection("lessons")
            .document(lessonId)
            .delete()
            .await()
    }

    override suspend fun getLessonById(id: String): Lesson? {
        var doc = db.collection("lessons").document(id).get().await()
        if (!doc.exists()) {
            val query = db.collection("lessons").whereEqualTo("id", id).limit(1).get().await()
            doc = query.documents.firstOrNull() ?: return null
        }
        return doc.toObject(Lesson::class.java)?.copy(id = doc.id)
    }

    override suspend fun getExperimentById(id: String): Experiment? {
        var doc = db.collection("experiments").document(id).get().await()
        if (!doc.exists()) {
            val query = db.collection("experiments").whereEqualTo("id", id).limit(1).get().await()
            doc = query.documents.firstOrNull() ?: return null
        }
        return doc.toObject(Experiment::class.java)?.copy(id = doc.id)
    }

    override suspend fun getLessonsByClassId(classId: String): List<Lesson> {
        val classSnap = db.collection("classes").document(classId).get().await()
        val lessonIds = (classSnap.get("lessonIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
        if (lessonIds.isEmpty()) return emptyList()

        val lessons = mutableListOf<Lesson>()
        for (lessonId in lessonIds) {
            val lesson = getLessonById(lessonId)
            if (lesson != null) lessons.add(lesson)
        }
        return lessons
    }


    suspend fun getExperimentDataForLesson(lessonId: String): Pair<List<Equipement>, List<Substance>> {
        val dbLessons = db.collection("lessons")
        val dbExperiments = db.collection("experiments")
        val dbEquipments = db.collection("equipments")
        val dbSubstances = db.collection("substances")

        // 1️⃣ Ia lecția (după docId sau după field "id")
        var lessonSnap = dbLessons.document(lessonId).get().await()
        if (!lessonSnap.exists()) {
            val query = dbLessons.whereEqualTo("id", lessonId).limit(1).get().await()
            lessonSnap = query.documents.firstOrNull() ?: return Pair(emptyList(), emptyList())
        }

        // 2️⃣ Ia lista de experimente
        val experimentIds = (lessonSnap.get("experimentIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
        if (experimentIds.isEmpty()) return Pair(emptyList(), emptyList())

        val firstExperimentId = experimentIds.first()

        // 3️⃣ Găsește experimentul (după docId sau după field "id")
        var experimentSnap = dbExperiments.document(firstExperimentId).get().await()
        if (!experimentSnap.exists()) {
            val query = dbExperiments.whereEqualTo("id", firstExperimentId).limit(1).get().await()
            experimentSnap = query.documents.firstOrNull() ?: return Pair(emptyList(), emptyList())
        }

        // 4️⃣ Extrage lista de id-uri de echipamente și substanțe
        val equipmentIds = (experimentSnap.get("equipmentIds") as? List<*>)?.filterIsInstance<String>().orEmpty()
        val substanceIds = (experimentSnap.get("substanceIds") as? List<*>)?.filterIsInstance<String>().orEmpty()

        // 5️⃣ Ia echipamentele
        val equipments = mutableListOf<Equipement>()
        for (eqId in equipmentIds) {
            var eqSnap = dbEquipments.document(eqId).get().await()
            if (!eqSnap.exists()) {
                val query = dbEquipments.whereEqualTo("id", eqId).limit(1).get().await()
                eqSnap = query.documents.firstOrNull() ?: continue
            }

            val eq = Equipement(
                id = eqSnap.id,
                name = eqSnap.getString("name").orEmpty()
            )
            equipments.add(eq)
        }

        // 6️⃣ Ia substanțele
        val substances = mutableListOf<Substance>()
        for (subId in substanceIds) {
            var subSnap = dbSubstances.document(subId).get().await()
            if (!subSnap.exists()) {
                val query = dbSubstances.whereEqualTo("id", subId).limit(1).get().await()
                subSnap = query.documents.firstOrNull() ?: continue
            }

            val sub = Substance(
                id = subSnap.id,
                name = subSnap.getString("name").orEmpty()
            )
            substances.add(sub)
        }

        // ✅ Returnăm echipamentele și substanțele
        return Pair(equipments, substances)
    }


}