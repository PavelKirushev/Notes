package com.example.note.data.room

import com.example.note.domain.Note

fun NoteEntity.toDomain(): Note {
    return Note(id = this.id, title = this.title, text = this.text)
}

fun Note.toEntity(userId: Long): NoteEntity {
    return NoteEntity(id = this.id, title = this.title, text = this.text, userId = userId)
}
