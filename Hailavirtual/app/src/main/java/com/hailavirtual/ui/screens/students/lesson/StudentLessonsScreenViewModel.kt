package com.hailavirtual.ui.screens.students.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance
import com.hailavirtual.domain.repo.LabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentLessonsScreenViewModel @Inject constructor(
    private val labRepository: LabRepository
) : ViewModel() {

    // Loading state
    var isLoading by mutableStateOf(false)
        private set

    // Dropdown states
    var isSubstancesExpanded by mutableStateOf(false)
        private set

    var isEquipmentExpanded by mutableStateOf(false)
        private set

    // Data for dropdowns
    var substances by mutableStateOf<List<Substance>>(emptyList())
        private set

    var equipments by mutableStateOf<List<Equipement>>(emptyList())
        private set

    // Selected items
    var selectedSubstance by mutableStateOf<Substance?>(null)
        private set

    var selectedEquipment by mutableStateOf<Equipement?>(null)
        private set

    /**
     * Called from StudentLessonsScreen when a lesson is opened.
     * For now it loads ALL substances & equipments.
     * Later you can filter by [lessonId] if you store per-lesson ids.
     */
    fun loadDataForLesson(lessonId: String?) {
        viewModelScope.launch {
            isLoading = true
            try {
                val allSubstances = labRepository.getSubstances()
                val allEquipments = labRepository.getEquipements()

                // TODO: if you have per-lesson ids, filter here using [lessonId]
                // e.g. val filteredSubstances = allSubstances.filter { it.id in lessonSubstanceIds }

                substances = allSubstances
                equipments = allEquipments
            } catch (_: Exception) {
                // here you could expose an error message state if you want
                substances = emptyList()
                equipments = emptyList()
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
