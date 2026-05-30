package com.example.note.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.api.AdminApiService
import com.example.note.data.api.SetSubscriptionRequest
import com.example.note.data.api.UserWithSubscriptionDto
import com.example.note.data.preferences.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

sealed class AdminUiState {
    object Loading : AdminUiState()
    data class Success(val users: List<UserWithSubscriptionDto>) : AdminUiState()
    data class Error(val message: String) : AdminUiState()
}

class AdminViewModel(
    private val api: AdminApiService,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _state = MutableStateFlow<AdminUiState>(AdminUiState.Loading)
    val state: StateFlow<AdminUiState> = _state.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    init { loadUsers() }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = AdminUiState.Loading
            try {
                val response = api.getUsers("Bearer ${tokenStorage.getToken()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    _state.value = AdminUiState.Success(response.body()!!.data ?: emptyList())
                } else {
                    _state.value = AdminUiState.Error("Не удалось загрузить пользователей")
                }
            } catch (e: Exception) {
                _state.value = AdminUiState.Error("Нет соединения с сервером")
            }
        }
    }

    // months — количество месяцев подписки (1, 3, 6, 12)
    fun setSubscription(userId: Long, months: Int) {
        viewModelScope.launch {
            try {
                val expiresAt = LocalDate.now()
                    .plusMonths(months.toLong())
                    .atStartOfDay(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_INSTANT)

                val response = api.setSubscription(
                    token = "Bearer ${tokenStorage.getToken()}",
                    userId = userId,
                    request = SetSubscriptionRequest(plan = "basic", expiresAt = expiresAt)
                )
                if (response.isSuccessful) {
                    loadUsers()
                } else {
                    _actionError.value = "Ошибка при выставлении подписки"
                }
            } catch (e: Exception) {
                _actionError.value = "Нет соединения с сервером"
            }
        }
    }

    fun cancelSubscription(userId: Long) {
        viewModelScope.launch {
            try {
                val response = api.cancelSubscription(
                    token = "Bearer ${tokenStorage.getToken()}",
                    userId = userId
                )
                if (response.isSuccessful) {
                    loadUsers()
                } else {
                    _actionError.value = "Ошибка при отмене подписки"
                }
            } catch (e: Exception) {
                _actionError.value = "Нет соединения с сервером"
            }
        }
    }

    fun clearActionError() { _actionError.value = null }
}
