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
    object Checking : TokenState()   // идёт проверка — показываем сплэш/пусто
    object Valid : TokenState()      // токен жив → главная
    object Invalid : TokenState()    // токена нет или истёк → логин
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Checking)
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

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
            _tokenState.value = if (valid) TokenState.Valid else TokenState.Invalid
        }
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun login(email: String, password: String) {
        if (!validate(email, password)) return
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _uiState.value = authRepository.login(email.trim(), password).fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Ошибка входа") }
            )
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
            _uiState.value = authRepository.register(email.trim(), password).fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Ошибка регистрации") }
            )
        }
    }

    fun logout() {
        authRepository.logout()
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
