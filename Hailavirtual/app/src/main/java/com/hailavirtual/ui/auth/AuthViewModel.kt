package com.hailavirtual.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hailavirtual.domain.repo.AuthRepository
import com.hailavirtual.core.session.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.hailavirtual.data.model.User
import com.hailavirtual.data.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Auth orchestration VM. Collects current user Flow,
 * exposes simple intents (signIn, signUp, signOut) and UI state for screens to observe.
 * All operations are cancellation-safe. */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val session: SessionManager
) : ViewModel() {

    val currentUser: StateFlow<User?> =
        repo.currentUser.stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null)

    init {
        viewModelScope.launch {
            session.isValid.collect { valid ->
                if (!valid && currentUser.value != null) {
                    repo.signOut()
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            session.clear()
            repo.signOut()
        }
    }
}