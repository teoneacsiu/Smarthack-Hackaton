package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance

interface LabRepository {
    suspend fun addEquipement(name: String)
    suspend fun addSubstance(name: String)
    suspend fun deleteEquipement(id: String)
    suspend fun deleteSubstance(id: String)
    suspend fun getEquipements(): List<Equipement>
    suspend fun getSubstances(): List<Substance>
    suspend fun getSubstanceById(id: String): Substance?
    suspend fun getEquipementById(id: String): Equipement?
}