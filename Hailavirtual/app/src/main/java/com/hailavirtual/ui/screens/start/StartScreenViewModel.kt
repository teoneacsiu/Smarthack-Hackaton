package com.hailavirtual.ui.screens.start

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class StartRole { PROFESOR, ELEV, ADMIN }

@HiltViewModel
class StartScreenViewModel @Inject constructor() : ViewModel() {
    var selectedRole: StartRole? by mutableStateOf(null)
        private set

    fun onProfesorClick() { selectedRole = StartRole.PROFESOR }
    fun onElevClick() { selectedRole = StartRole.ELEV }
    fun onAdminClick() { selectedRole = StartRole.ADMIN }

    fun clearSelection() { selectedRole = null }
}