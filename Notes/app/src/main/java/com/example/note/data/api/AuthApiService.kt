package com.example.note.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<ApiResponse<AuthResponseData>>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<ApiResponse<AuthResponseData>>

    @GET("api/v1/auth/me")
    suspend fun me(@Header("Authorization") token: String): Response<ApiResponse<UserDto>>

    @GET("api/v1/subscriptions/me")
    suspend fun getMySubscription(@Header("Authorization") token: String): Response<ApiResponse<MySubscriptionResponse>>
}
