package com.hailavirtual.ui.screens.students.lesson

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance
import com.hailavirtual.domain.repo.LabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsScreenViewModel @Inject constructor(
    private val labRepository: LabRepository
) : ViewModel() {

    var isSubstancesExpanded by mutableStateOf(false)
        private set

    var isEquipmentExpanded by mutableStateOf(false)
        private set

    var selectedSubstance by mutableStateOf<Substance?>(null)
        private set

    var selectedEquipment by mutableStateOf<Equipement?>(null)
        private set

    var substances by mutableStateOf<List<Substance>>(emptyList())
        private set

    var equipments by mutableStateOf<List<Equipement>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true
            try {
                substances = labRepository.getSubstances()
                equipments = labRepository.getEquipments()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleSubstancesDropdown() {
        isSubstancesExpanded = !isSubstancesExpanded
        if (isSubstancesExpanded) isEquipmentExpanded = false
    }

    fun toggleEquipmentDropdown() {
        isEquipmentExpanded = !isEquipmentExpanded
        if (isEquipmentExpanded) isSubstancesExpanded = false
    }

    fun selectSubstance(item: Substance) {
        selectedSubstance = item
        isSubstancesExpanded = false
    }

    fun selectEquipment(item: Equipement) {
        selectedEquipment = item
        isEquipmentExpanded = false
    }
}