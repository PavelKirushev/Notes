package com.example.note.domain.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
    fun logout()
}
