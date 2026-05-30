package com.example.note.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApiService {

    @GET("api/v1/admin/users")
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<UserWithSubscriptionDto>>>

    @PUT("api/v1/admin/users/{userId}/subscription")
    suspend fun setSubscription(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body request: SetSubscriptionRequest
    ): Response<ApiResponse<SubscriptionDto>>

    @DELETE("api/v1/admin/users/{userId}/subscription")
    suspend fun cancelSubscription(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long
    ): Response<ApiResponse<Any>>
}
