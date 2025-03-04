package com.example.notes.data

import com.example.notes.domain.Note
import com.example.data.NoteEntity

//две функции нужны для преобразования данных удобных для UI и базы данных
fun NoteEntity.toDomain(): Note {
    return Note(id = this.id, title = this.title, text = this.text,)
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(id = this.id, title = this.title, text = this.text,)
}