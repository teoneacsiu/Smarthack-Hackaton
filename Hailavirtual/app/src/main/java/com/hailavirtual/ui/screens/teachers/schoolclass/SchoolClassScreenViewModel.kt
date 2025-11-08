package com.hailavirtual.ui.screens.teachers.schoolclass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiClass(
    val name: String,
    val id: String,
    val isHighlighted: Boolean = false   // pentru culoare diferita la icon
)

@HiltViewModel
class SchoolClassScreenViewModel @Inject constructor() : ViewModel() {

    var classes: List<UiClass> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            classes = listOf(
                UiClass("Clasa 7B", "7B23"),
                UiClass("Clasa 8A", "8A25"),
                UiClass("Clasa 6A", "6A28", isHighlighted = true)
            )
        }
    }

}