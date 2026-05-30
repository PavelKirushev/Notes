package com.example.note.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.presentation.admin.AdminScreen
import com.example.note.presentation.auth.AuthViewModel
import com.example.note.presentation.auth.LoginScreen
import com.example.note.presentation.auth.RegisterScreen
import com.example.note.presentation.auth.TokenState
import com.example.note.presentation.mainscreen.NotesScreen
import com.example.note.presentation.noteWindow.NoteWindow
import org.koin.androidx.compose.koinViewModel

private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val HOME_ROUTE = "home"
private const val ADMIN_ROUTE = "admin"
private const val NOTE_DETAILS_ROUTE = "details/{noteId}"
private const val NOTE_ID_ROUTE = "noteId"

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigator(
    authViewModel: AuthViewModel,
    controller: NavHostController
) {
    val tokenState by authViewModel.tokenState.collectAsState()

    if (tokenState is TokenState.Checking) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    val startDestination = if (tokenState is TokenState.Valid) HOME_ROUTE else LOGIN_ROUTE

    NavHost(navController = controller, startDestination = startDestination) {

        composable(LOGIN_ROUTE) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    controller.navigate(HOME_ROUTE) {
                        popUpTo(LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = { controller.navigate(REGISTER_ROUTE) }
            )
        }

        composable(REGISTER_ROUTE) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    controller.navigate(HOME_ROUTE) {
                        popUpTo(LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    authViewModel.resetState()
                    controller.popBackStack()
                }
            )
        }

        composable(HOME_ROUTE) {
            // MainViewModel создаётся здесь — скопирован к этому nav-entry.
            // При навигации на HOME после смены пользователя entry пересоздаётся
            // → новый MainViewModel → новая подписка на Room с актуальным userId.
            val mainViewModel: MainViewModel = koinViewModel()
            val isSubscribed by authViewModel.isSubscribed.collectAsState()

            NotesScreen(
                controller = controller,
                mainViewModel = mainViewModel,
                isSuper = authViewModel.isSuper(),
                isSubscribed = isSubscribed,
                onAdminClick = { controller.navigate(ADMIN_ROUTE) },
                onLogout = {
                    authViewModel.logout()
                    controller.navigate(LOGIN_ROUTE) {
                        popUpTo(HOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(NOTE_DETAILS_ROUTE) {
            val mainViewModel: MainViewModel = koinViewModel()
            val isSubscribed by authViewModel.isSubscribed.collectAsState()
            val noteId = it.arguments?.getString(NOTE_ID_ROUTE)?.toIntOrNull()
            if (noteId != null) {
                val note = mainViewModel.noteListFlow.value.find { note -> note.id == noteId }
                if (note != null) {
                    NoteWindow(controller, note, mainViewModel, isSubscribed)
                }
            }
        }

        composable(ADMIN_ROUTE) {
            AdminScreen(controller = controller)
        }
    }

    LaunchedEffect(tokenState) {
        if (tokenState is TokenState.Invalid &&
            controller.currentDestination?.route != LOGIN_ROUTE &&
            controller.currentDestination?.route != REGISTER_ROUTE
        ) {
            controller.navigate(LOGIN_ROUTE) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
