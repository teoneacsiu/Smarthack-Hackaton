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

    override suspend fun getSubstances(): List<Substance> {
        val snap = db.collection("substances").get().await()
        return snap.toObjects(Substance::class.java)
    }

    override suspend fun getEquipements(): List<Equipement> {
        val snap = db.collection("equipments").get().await()
        return snap.toObjects(Equipement::class.java)
    }

    override suspend fun getSubstanceById(id: String): Substance? {
        var doc = db.collection("substances").document(id).get().await()
        if (!doc.exists()) {
            val query = db.collection("substances").whereEqualTo("id", id).limit(1).get().await()
            doc = query.documents.firstOrNull() ?: return null
        }
        return doc.toObject(Substance::class.java)?.copy(id = doc.id)
    }

    override suspend fun getEquipementById(id: String): Equipement? {
        var doc = db.collection("equipments").document(id).get().await()
        if (!doc.exists()) {
            val query = db.collection("equipments").whereEqualTo("id", id).limit(1).get().await()
            doc = query.documents.firstOrNull() ?: return null
        }
        return doc.toObject(Equipement::class.java)?.copy(id = doc.id)
    }
}