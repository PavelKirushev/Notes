package com.example.note.domain.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun validateToken(): Boolean
    suspend fun checkSubscription(): Boolean
    fun isLoggedIn(): Boolean
    fun isSuper(): Boolean
    fun logout()
}
