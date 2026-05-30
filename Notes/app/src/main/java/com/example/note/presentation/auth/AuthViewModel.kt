package com.example.note.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    object Success : AuthUiState()
}

sealed class TokenState {
    object Checking : TokenState()
    object Valid : TokenState()
    object Invalid : TokenState()
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Checking)
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    // true если подписка активна ИЛИ пользователь суперпользователь
    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed: StateFlow<Boolean> = _isSubscribed.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        if (!authRepository.isLoggedIn()) {
            _tokenState.value = TokenState.Invalid
            return
        }
        viewModelScope.launch {
            val valid = authRepository.validateToken()
            if (valid) {
                _tokenState.value = TokenState.Valid
                loadSubscriptionStatus()
            } else {
                _tokenState.value = TokenState.Invalid
            }
        }
    }

    private suspend fun loadSubscriptionStatus() {
        _isSubscribed.value = authRepository.isSuper() || authRepository.checkSubscription()
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun isSuper(): Boolean = authRepository.isSuper()

    fun login(email: String, password: String) {
        if (!validate(email, password)) return
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Ошибка входа") }
            )
            if (result.isSuccess) loadSubscriptionStatus()
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Пароли не совпадают")
            return
        }
        if (!validate(email, password)) return
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.register(email.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Ошибка регистрации") }
            )
            if (result.isSuccess) loadSubscriptionStatus()
        }
    }

    fun logout() {
        authRepository.logout()
        _isSubscribed.value = false
        _uiState.value = AuthUiState.Idle
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun validate(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _uiState.value = AuthUiState.Error("Введите email")
                false
            }
            password.length < 8 -> {
                _uiState.value = AuthUiState.Error("Пароль минимум 8 символов")
                false
            }
            else -> true
        }
    }
}
