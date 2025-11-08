package com.hailavirtual.ui.screens.students.customexp

import androidx.lifecycle.ViewModel
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
}