package com.example.notes.data

import com.example.notes.domain.Note
import com.example.data.NoteEntity

/**
 * Two functions for transformation the data needed for the UI and the DB
 *
 */
fun NoteEntity.toDomain(): Note {
    return Note(id = this.id, title = this.title, text = this.text,)
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(id = this.id, title = this.title, text = this.text,)
}