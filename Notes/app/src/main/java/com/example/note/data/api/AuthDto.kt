package com.example.note.data.api

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val email: String,
    val password: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String?
)

data class AuthResponseData(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val email: String,
    @SerializedName("is_super") val isSuper: Boolean,
    @SerializedName("created_at") val createdAt: String
)

data class MySubscriptionResponse(
    val active: Boolean,
    val subscription: Any?
)
