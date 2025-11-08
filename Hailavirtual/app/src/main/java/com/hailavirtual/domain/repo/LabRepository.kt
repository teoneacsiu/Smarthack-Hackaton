package com.hailavirtual.domain.repo

interface LabRepository {
    suspend fun addEquipment(name: String)
    suspend fun addSubstance(name: String)
    suspend fun deleteEquipment(id: String)
    suspend fun deleteSubstance(id: String)
}