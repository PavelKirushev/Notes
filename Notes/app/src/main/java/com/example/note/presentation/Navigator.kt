package com.example.note.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.presentation.auth.AuthViewModel
import com.example.note.presentation.auth.LoginScreen
import com.example.note.presentation.auth.RegisterScreen
import com.example.note.presentation.mainscreen.NotesScreen
import com.example.note.presentation.noteWindow.NoteWindow

private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val HOME_ROUTE = "home"
private const val NOTE_DETAILS_ROUTE = "details/{noteId}"
private const val NOTE_ID_ROUTE = "noteId"
private const val DETAILS_PREFIX = "details/"

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigator(
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    controller: NavHostController
) {
    // Если токен уже есть — сразу открываем заметки, форму входа не показываем
    val startDestination = if (authViewModel.isLoggedIn()) HOME_ROUTE else LOGIN_ROUTE

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
            NotesScreen(
                controller = controller,
                mainViewModel = mainViewModel,
                onLogout = {
                    authViewModel.logout()
                    controller.navigate(LOGIN_ROUTE) {
                        popUpTo(HOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(NOTE_DETAILS_ROUTE) {
            val noteId = it.arguments?.getString(NOTE_ID_ROUTE)?.toIntOrNull()
            if (noteId != null) {
                val note = mainViewModel.noteListFlow.value.find { note -> note.id == noteId }
                if (note != null) {
                    NoteWindow(controller, note, mainViewModel)
                }
            }
        }
    }
}
