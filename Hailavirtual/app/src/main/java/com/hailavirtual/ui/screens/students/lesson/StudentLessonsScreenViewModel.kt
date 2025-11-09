package com.hailavirtual.ui.screens.students.lesson


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance
import com.hailavirtual.domain.repo.LabRepository
import com.hailavirtual.domain.repo.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentLessonsScreenViewModel @Inject constructor(
    private val labRepository: LabRepository,
    private val lessonRepository: LessonRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isSubstancesExpanded by mutableStateOf(false)
        private set

    var isEquipmentExpanded by mutableStateOf(false)
        private set

    var substances by mutableStateOf<List<Substance>>(emptyList())
        private set

    var equipments by mutableStateOf<List<Equipement>>(emptyList())
        private set

    var selectedSubstance by mutableStateOf<Substance?>(null)
        private set

    var selectedEquipment by mutableStateOf<Equipement?>(null)
        private set

    /**
     * Încarcă substanțele și echipamentele pentru lecția selectată.
     * 1️⃣ Ia lecția.
     * 2️⃣ Ia primul experiment.
     * 3️⃣ Încarcă echipamentele și substanțele corespunzătoare.
     */
    fun loadDataForLesson(lessonId: String?) {
        if (lessonId.isNullOrBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                val lesson = lessonRepository.getLessonById(lessonId)
                if (lesson == null || lesson.experimentIds.isEmpty()) {
                    substances = emptyList()
                    equipments = emptyList()
                    isLoading = false
                    return@launch
                }

                val firstExperimentId = lesson.experimentIds.first()
                val experiment = lessonRepository.getExperimentById(firstExperimentId)
                if (experiment == null) {
                    substances = emptyList()
                    equipments = emptyList()
                    isLoading = false
                    return@launch
                }

                // Încarcă substanțele
                val loadedSubstances = mutableListOf<Substance>()
                for (subId in experiment.substanceIds) {
                    val sub = labRepository.getSubstanceById(subId)
                    Log.d("LOAD SUCCEEEDED", sub?.name ?: "Null")
                    if (sub != null) loadedSubstances.add(sub)
                }

                // Încarcă echipamentele
                val loadedEquipements = mutableListOf<Equipement>()
                for (eqId in experiment.equipmentIds) {
                    val eq = labRepository.getEquipementById(eqId)
                    Log.d("LOAD SUCCEEEDED", eq?.name ?: "Null")
                    if (eq != null) loadedEquipements.add(eq)
                }

                substances = loadedSubstances
                equipments = loadedEquipements

            } catch (e: Exception) {
                e.printStackTrace()
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