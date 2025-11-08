package com.hailavirtual.ui.screens.teachers.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TeacherLessonUi(
    val title: String,
    val subtitle: String,
    val color: Color
)

@HiltViewModel
class TeacherLessonsScreenViewModel @Inject constructor() : ViewModel() {
    var lessons: List<TeacherLessonUi> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            lessons = listOf(
                TeacherLessonUi("Laborator - Siguranta", "Reguli si proceduri", Color(0xFF0000CC)),
                TeacherLessonUi("Solutii si reactii", "Experimente introductive", Color(0xFF3C0F84)),
                TeacherLessonUi("Evaluare rapida", "Mini-quiz", Color(0xFFcf0072))
            )
        }
    }
}