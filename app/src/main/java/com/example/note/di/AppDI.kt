package com.example.note.di

import org.koin.dsl.module

val appModule = module {

    single <com.example.presentation.MainViewModel>{
        com.example.presentation.MainViewModel(
            addNoteUseCase = get(),
            editNoteUseCase = get(),
            getNoteListUseCase = get(),
            removeNoteUseCase = get()
        )
    }
}