package com.example.note.presentation.noteWindow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.api.AuthApiService
import com.example.note.data.api.SummaryRequest
import com.example.note.data.preferences.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SummaryState {
    object Idle : SummaryState()
    object Loading : SummaryState()
    data class Success(val summary: String) : SummaryState()
    data class Error(val message: String) : SummaryState()
}

class SummaryViewModel(
    private val api: AuthApiService,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _state = MutableStateFlow<SummaryState>(SummaryState.Idle)
    val state: StateFlow<SummaryState> = _state.asStateFlow()

    fun summarize(text: String) {
        if (text.isBlank()) {
            _state.value = SummaryState.Error("Заметка пустая")
            return
        }
        viewModelScope.launch {
            _state.value = SummaryState.Loading
            try {
                val response = api.summarize(
                    token = "Bearer ${tokenStorage.getToken()}",
                    request = SummaryRequest(text)
                )
                _state.value = if (response.isSuccessful && response.body()?.success == true) {
                    SummaryState.Success(response.body()!!.data!!.summary)
                } else {
                    SummaryState.Error("Не удалось создать summary")
                }
            } catch (e: Exception) {
                _state.value = SummaryState.Error("Нет соединения с сервером")
            }
        }
    }

    fun reset() {
        _state.value = SummaryState.Idle
    }
}
