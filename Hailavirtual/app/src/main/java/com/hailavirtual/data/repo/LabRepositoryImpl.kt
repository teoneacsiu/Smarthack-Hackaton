package com.hailavirtual.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hailavirtual.domain.repo.LabRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LabRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : LabRepository {

    override suspend fun addEquipment(name: String) {
        val doc = db.collection("equipment").document()
        val data = mapOf(
            "name" to name,
            "createdBy" to FirebaseAuth.getInstance().currentUser?.uid
        )
        doc.set(data).await()
    }

    override suspend fun addSubstance(name: String) {
        val doc = db.collection("substances").document()
        val data = mapOf(
            "name" to name,
            "createdBy" to FirebaseAuth.getInstance().currentUser?.uid
        )
        doc.set(data).await()
    }

    override suspend fun deleteEquipment(id: String) {
        db.collection("equipment").document(id).delete().await()
    }

    override suspend fun deleteSubstance(id: String) {
        db.collection("substances").document(id).delete().await()
    }
}