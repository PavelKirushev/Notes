package com.example.note.di

import com.example.note.data.api.AuthApiService
import com.example.note.data.auth.AuthRepositoryImpl
import com.example.note.data.preferences.TokenStorage
import com.example.note.domain.auth.AuthRepository
import com.example.note.presentation.auth.AuthViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Для эмулятора Android: 10.0.2.2 = localhost хост-машины
// Для реального устройства: замени на IP компьютера в локальной сети (напр. 192.168.1.10)
private const val BASE_URL = "http://192.168.1.10:8080/"

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

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    single { TokenStorage(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single { AuthViewModel(get()) }
}
