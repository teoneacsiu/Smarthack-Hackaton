package com.hailavirtual.ui.screens.students.chooseclass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.SchoolClass
import com.hailavirtual.domain.repo.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseClassScreenViewModel @Inject constructor(
    private val classRepository: ClassRepository
) : ViewModel() {

    var classId by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun onClassIdChange(value: String) {
        classId = value.trim()
        error = null
    }

    fun tryEnter(onSuccess: (SchoolClass) -> Unit) {
        val id = classId
        if (id.isBlank()) {
            error = "Introdu un ID de clasa"
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val schoolClass = classRepository.getClassById(id)
                if (schoolClass != null) {
                    onSuccess(schoolClass)
                } else {
                    error = "ID de clasa invalid sau inexistent"
                }
            } catch (t: Throwable) {
                error = t.message ?: "Eroare la verificare. Incearca din nou."
            } finally {
                isLoading = false
            }
        }
    }
}
