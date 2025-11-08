package com.hailavirtual.ui.screens.students.endlesson

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EndLessonViewModel @Inject constructor(): ViewModel() {
    fun onFinish() { /* TODO: persist and navigate */ }
    fun onRepeat() { /* TODO: restart flow */ }
    fun onViewResults() { /* TODO: navigate to results */ }
}