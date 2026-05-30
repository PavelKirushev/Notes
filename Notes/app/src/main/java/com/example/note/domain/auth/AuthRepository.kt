package com.example.note.domain.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun validateToken(): Boolean
    fun isLoggedIn(): Boolean
    fun logout()
}
