package com.hailavirtual.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.data.model.GeminiResult
import com.hailavirtual.domain.repo.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor(
    private val repo: GeminiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<GeminiResult>(GeminiResult.Waiting)
    val state: StateFlow<GeminiResult> = _state

    fun run(prompt: String) = viewModelScope.launch {
        repo.generate(prompt).collect { _state.value = it }
    }
}