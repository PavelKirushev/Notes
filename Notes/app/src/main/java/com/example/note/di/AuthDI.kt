package com.example.note.di

import com.example.note.data.api.AdminApiService
import com.example.note.data.api.AuthApiService
import com.example.note.data.auth.AuthRepositoryImpl
import com.example.note.data.preferences.TokenStorage
import com.example.note.domain.auth.AuthRepository
import com.example.note.presentation.admin.AdminViewModel
import com.example.note.presentation.auth.AuthViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://172.20.10.13:8080/"

val authModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    // Retrofit регистрируем отдельно — чтобы оба сервиса могли его использовать
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(AuthApiService::class.java) }

    single { get<Retrofit>().create(AdminApiService::class.java) }

    single { TokenStorage(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    viewModel { AuthViewModel(get()) }

    viewModel { AdminViewModel(get(), get()) }
}
