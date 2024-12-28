package com.example.note.di

import com.example.presentation.presentation.MainViewModel
import org.koin.dsl.module

val appModule = module {

    single <com.example.presentation.presentation.MainViewModel>{
        com.example.presentation.presentation.MainViewModel(
            addNoteUseCase = get(),
            editNoteUseCase = get(),
            getNoteUseCase = get(),
            getNoteListUseCase = get(),
            removeNoteUseCase = get()
        )
    }
}