package com.hailavirtual.ui.screens.students.chooseclass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseClassScreenViewModel @Inject constructor() : ViewModel() {
    var classId: String by mutableStateOf("")
        private set

    fun onClassIdChange(value: String) { classId = value }
    fun canJoin(): Boolean = classId.isNotBlank()
}