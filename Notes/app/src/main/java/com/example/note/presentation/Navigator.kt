package com.example.note.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.presentation.mainscreen.NotesScreen
import com.example.note.presentation.noteWindow.NoteWindow


/**
 * Composable function for navigation in app
 *
 *  home - main screen (NotesScreen.kt)
 *
 *  details - window for editing note (NoteWindow.kt)
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigator(mainViewModel: MainViewModel, controller: NavHostController, ){
    NavHost(navController = controller, startDestination = HOME_ROUTE) {
        composable(HOME_ROUTE) { NotesScreen(controller, mainViewModel) }
        composable(NOTE_DETAILS_ROUTE) {
            val noteId = it.arguments?.getString(NOTE_ID_ROUTE)?.toIntOrNull()
            if (noteId != null) {
                val note = mainViewModel.noteListFlow.value.find { it.id == noteId }
                if (note != null) {
                    NoteWindow(controller, note, mainViewModel)
                }
            }

        }
    }
}

private const val HOME_ROUTE = "home"
private const val NOTE_DETAILS_ROUTE = "details/{noteId}"
private const val NOTE_ID_ROUTE = "noteId"
