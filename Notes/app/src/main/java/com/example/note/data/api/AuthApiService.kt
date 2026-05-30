package com.example.note.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<ApiResponse<AuthResponseData>>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<ApiResponse<AuthResponseData>>
}
