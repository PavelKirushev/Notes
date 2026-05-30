package com.example.note.data.api

import com.google.gson.annotations.SerializedName

data class UserWithSubscriptionDto(
    val id: Long,
    val email: String,
    @SerializedName("is_super") val isSuper: Boolean,
    val subscription: SubscriptionDto?,
    @SerializedName("has_active") val hasActive: Boolean
)

data class SubscriptionDto(
    val status: String,
    val plan: String,
    @SerializedName("expires_at") val expiresAt: String?
)

data class SetSubscriptionRequest(
    val plan: String,
    @SerializedName("expires_at") val expiresAt: String
)
