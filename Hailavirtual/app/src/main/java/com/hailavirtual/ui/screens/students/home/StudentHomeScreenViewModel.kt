package com.hailavirtual.ui.screens.students.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonUi(
    val title: String,
    val subtitle: String,
    val color: Color
)

@HiltViewModel
class StudentHomeScreenViewModel @Inject constructor() : ViewModel() {
    var lessons: List<LessonUi> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            lessons = listOf(
                LessonUi("Lectia 3 -", "Lorem ipsum", Color(0xFF0000CC)),
                LessonUi("Lectia 2 -", "Lorem ipsum", Color(0xFF3C0F84)),
                LessonUi("Lectia 1 -", "Lorem ipsum", Color(0xFFcf0072))
            )
        }
    }
}