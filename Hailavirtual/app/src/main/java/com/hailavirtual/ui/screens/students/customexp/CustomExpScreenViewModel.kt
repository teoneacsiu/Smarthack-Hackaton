package com.hailavirtual.ui.screens.students.customexp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hailavirtual.data.model.Equipement
import com.hailavirtual.data.model.Substance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class CustomExpScreenViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isChatVisible = MutableStateFlow(false)
    val isChatVisible: StateFlow<Boolean> = _isChatVisible

    fun toggleChat() {
        _isChatVisible.update { !it }
        if (!_isChatVisible.value) {
            _messages.value = emptyList() // Reset chat when closed
        }
    }

    fun addMessage(message: ChatMessage) {
        _messages.update { it + message }
    }

    fun clearChat() {
        _messages.value = emptyList()
    }

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

    var dashboardSubstances by mutableStateOf<List<Substance>>(emptyList())
        private set

    fun toggleSubstancesDropdown() {
        isSubstancesExpanded = !isSubstancesExpanded
        if (isSubstancesExpanded) isEquipmentExpanded = false
    }

    fun selectSubstance(item: Substance) {
        // pastram pentru textul de sus
        selectedSubstance = item
        isSubstancesExpanded = false

        // 🔹 adaugam substanta si pe dashboard (nu o inlocuim)
        dashboardSubstances = dashboardSubstances + item
    }

    fun toggleEquipmentDropdown() {
        isEquipmentExpanded = !isEquipmentExpanded
        if (isEquipmentExpanded) isSubstancesExpanded = false
    }

    fun selectEquipment(item: Equipement) {
        selectedEquipment = item
        isEquipmentExpanded = false
    }
}