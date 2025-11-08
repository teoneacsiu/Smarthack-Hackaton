package com.hailavirtual.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance
import com.hailavirtual.domain.repo.LabRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LabRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : LabRepository {

    override suspend fun addEquipement(name: String) {
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

    override suspend fun deleteEquipement(id: String) {
        db.collection("equipment").document(id).delete().await()
    }

    override suspend fun deleteSubstance(id: String) {
        db.collection("substances").document(id).delete().await()
    }

    // NOU: citire echipamente
    override suspend fun getEquipements(): List<Equipement> {
        val snap = db.collection("equipment")
            .get()
            .await()

        return snap.documents.map { doc ->
            Equipement(
                id = doc.id,
                name = doc.getString("name") ?: ""
            )
        }
    }

    // NOU: citire substante
    override suspend fun getSubstances(): List<Substance> {
        val snap = db.collection("substances")
            .get()
            .await()

        return snap.documents.map { doc ->
            Substance(
                id = doc.id,
                name = doc.getString("name") ?: ""
            )
        }
    }
}