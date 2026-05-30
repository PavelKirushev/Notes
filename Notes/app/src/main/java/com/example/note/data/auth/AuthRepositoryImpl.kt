package com.example.note.data.auth

import com.example.note.data.api.AuthApiService
import com.example.note.data.api.AuthRequest
import com.example.note.data.preferences.TokenStorage
import com.example.note.domain.auth.AuthRepository
import org.json.JSONObject

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = api.login(AuthRequest(email, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()!!.data!!
                tokenStorage.saveToken(data.token)
                tokenStorage.saveIsSuper(data.user.isSuper)
                tokenStorage.saveUserId(data.user.id)
                Result.success(Unit)
            } else {
                val message = parseErrorBody(response.errorBody()?.string())
                    ?: response.body()?.error
                    ?: "Неверный логин или пароль"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения с сервером"))
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val response = api.register(AuthRequest(email, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()!!.data!!
                tokenStorage.saveToken(data.token)
                tokenStorage.saveIsSuper(data.user.isSuper)
                tokenStorage.saveUserId(data.user.id)
                Result.success(Unit)
            } else {
                val message = parseErrorBody(response.errorBody()?.string())
                    ?: response.body()?.error
                    ?: "Ошибка регистрации"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения с сервером"))
        }
    }

    override suspend fun validateToken(): Boolean {
        val token = tokenStorage.getToken() ?: return false
        return try {
            val response = api.me("Bearer $token")
            if (response.code() == 401) {
                tokenStorage.clearToken()
                false
            } else {
                response.isSuccessful
            }
        } catch (e: Exception) {
            // Нет сети — считаем токен валидным, не разлогиниваем
            true
        }
    }

    override fun isLoggedIn(): Boolean = tokenStorage.hasToken()

    override fun isSuper(): Boolean = tokenStorage.isSuper()

    override fun logout() = tokenStorage.clearToken()

    private fun parseErrorBody(body: String?): String? {
        if (body.isNullOrBlank()) return null
        return try {
            JSONObject(body).optString("error").takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}
