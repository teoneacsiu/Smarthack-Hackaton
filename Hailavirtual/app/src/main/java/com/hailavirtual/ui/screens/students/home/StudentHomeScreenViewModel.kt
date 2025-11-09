package com.hailavirtual.ui.screens.students.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.Lesson
import com.hailavirtual.domain.repo.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonUi(
    val title: String,
    val subtitle: String,
    val color: Color
)

@HiltViewModel
class StudentHomeScreenViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val classId: String = checkNotNull(savedStateHandle["classId"])

    var lessons by mutableStateOf<List<Lesson>>(emptyList())
        private set

    init {
        loadLessons()
    }

    private fun loadLessons() {
        viewModelScope.launch {
            lessons = lessonRepository.getLessonsByClassId(classId)
        }
    }
}