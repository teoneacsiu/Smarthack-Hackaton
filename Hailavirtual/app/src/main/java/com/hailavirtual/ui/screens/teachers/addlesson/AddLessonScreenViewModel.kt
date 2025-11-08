package com.hailavirtual.ui.screens.teachers.addlesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddLessonScreenViewModel @Inject constructor() : ViewModel() {
    var title: String by mutableStateOf("")
        private set

    fun onTitleChange(v: String) { title = v }
}