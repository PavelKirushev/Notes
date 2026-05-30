package com.example.note.di

import com.example.note.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // viewModel (не single!) — пересоздаётся при каждом входе нового пользователя,
    // чтобы заново подписаться на Room Flow с актуальным userId
    viewModel {
        MainViewModel(
            addNoteUseCase = get(),
            editNoteUseCase = get(),
            getNoteListUseCase = get(),
            removeNoteUseCase = get()
        )
    }
}
